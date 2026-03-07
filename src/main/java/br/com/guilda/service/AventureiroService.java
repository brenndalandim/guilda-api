package br.com.guilda.service;

import br.com.guilda.dto.AventureiroCreateRequest;
import br.com.guilda.dto.AventureiroDetalheResponse;
import br.com.guilda.dto.AventureiroResumoResponse;
import br.com.guilda.dto.AventureiroUpdateRequest;
import br.com.guilda.dto.CompanheiroRequest;
import br.com.guilda.dto.CompanheiroResponse;
import br.com.guilda.exception.InvalidRequestException;
import br.com.guilda.exception.ResourceNotFoundException;
import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.ClasseAventureiro;
import br.com.guilda.model.Companheiro;
import br.com.guilda.model.EspecieCompanheiro;
import br.com.guilda.repository.AventureiroRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class AventureiroService {

    private final AventureiroRepository repository;

    public AventureiroService(AventureiroRepository repository) {
        this.repository = repository;
    }

    public AventureiroDetalheResponse criar(AventureiroCreateRequest request) {
        validarAventureiro(request.getNome(), request.getClasse(), request.getNivel());

        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setNome(request.getNome().trim());
        aventureiro.setClasse(ClasseAventureiro.valueOf(request.getClasse()));
        aventureiro.setNivel(request.getNivel());
        aventureiro.setAtivo(true);

        repository.save(aventureiro);
        return toDetalheResponse(aventureiro);
    }

    public AventureiroDetalheResponse buscarPorId(Long id) {
        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        return toDetalheResponse(aventureiro);
    }

    public List<AventureiroResumoResponse> listar(String classe, Boolean ativo, Integer nivelMinimo, Integer page, Integer size) {
        validarPaginacao(page, size);

        Stream<Aventureiro> stream = repository.findAll().stream();

        if (classe != null && !classe.isBlank()) {
            List<String> erros = new ArrayList<>();
            ClasseAventureiro classeEnum = parseClasse(classe, erros);
            if (!erros.isEmpty()) {
                throw new InvalidRequestException("Solicitação inválida", erros);
            }
            stream = stream.filter(a -> a.getClasse() == classeEnum);
        }

        if (ativo != null) {
            stream = stream.filter(a -> a.getAtivo().equals(ativo));
        }

        if (nivelMinimo != null) {
            stream = stream.filter(a -> a.getNivel() >= nivelMinimo);
        }

        List<Aventureiro> filtrados = stream
                .sorted(Comparator.comparing(Aventureiro::getId))
                .toList();

        int fromIndex = page * size;
        if (fromIndex >= filtrados.size()) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(fromIndex + size, filtrados.size());

        return filtrados.subList(fromIndex, toIndex).stream()
                .map(this::toResumoResponse)
                .toList();
    }

    public int contarFiltrados(String classe, Boolean ativo, Integer nivelMinimo) {
        Stream<Aventureiro> stream = repository.findAll().stream();

        if (classe != null && !classe.isBlank()) {
            List<String> erros = new ArrayList<>();
            ClasseAventureiro classeEnum = parseClasse(classe, erros);
            if (!erros.isEmpty()) {
                throw new InvalidRequestException("Solicitação inválida", erros);
            }
            stream = stream.filter(a -> a.getClasse() == classeEnum);
        }

        if (ativo != null) {
            stream = stream.filter(a -> a.getAtivo().equals(ativo));
        }

        if (nivelMinimo != null) {
            stream = stream.filter(a -> a.getNivel() >= nivelMinimo);
        }

        return (int) stream.count();
    }

    public AventureiroDetalheResponse atualizar(Long id, AventureiroUpdateRequest request) {
        validarAventureiro(request.getNome(), request.getClasse(), request.getNivel());

        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        aventureiro.setNome(request.getNome().trim());
        aventureiro.setClasse(ClasseAventureiro.valueOf(request.getClasse()));
        aventureiro.setNivel(request.getNivel());

        repository.save(aventureiro);
        return toDetalheResponse(aventureiro);
    }

    public void encerrarVinculo(Long id) {
        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        aventureiro.setAtivo(false);
        repository.save(aventureiro);
    }

    public void recrutarNovamente(Long id) {
        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        aventureiro.setAtivo(true);
        repository.save(aventureiro);
    }

    public AventureiroDetalheResponse definirCompanheiro(Long id, CompanheiroRequest request) {
        validarCompanheiro(request);

        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        Companheiro companheiro = new Companheiro(
                request.getNome().trim(),
                EspecieCompanheiro.valueOf(request.getEspecie()),
                request.getLealdade()
        );

        aventureiro.setCompanheiro(companheiro);
        repository.save(aventureiro);

        return toDetalheResponse(aventureiro);
    }

    public void removerCompanheiro(Long id) {
        Aventureiro aventureiro = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        aventureiro.setCompanheiro(null);
        repository.save(aventureiro);
    }

    private AventureiroResumoResponse toResumoResponse(Aventureiro a) {
        return new AventureiroResumoResponse(
                a.getId(),
                a.getNome(),
                a.getClasse().name(),
                a.getNivel(),
                a.getAtivo()
        );
    }

    private AventureiroDetalheResponse toDetalheResponse(Aventureiro a) {
        CompanheiroResponse companheiroResponse = null;

        if (a.getCompanheiro() != null) {
            companheiroResponse = new CompanheiroResponse(
                    a.getCompanheiro().getNome(),
                    a.getCompanheiro().getEspecie().name(),
                    a.getCompanheiro().getLealdade()
            );
        }

        return new AventureiroDetalheResponse(
                a.getId(),
                a.getNome(),
                a.getClasse().name(),
                a.getNivel(),
                a.getAtivo(),
                companheiroResponse
        );
    }

    private void validarAventureiro(String nome, String classe, Integer nivel) {
        List<String> erros = new ArrayList<>();

        if (nome == null || nome.isBlank()) {
            erros.add("nome é obrigatório");
        }

        if (classe == null || classe.isBlank()) {
            erros.add("classe é obrigatória");
        } else {
            parseClasse(classe, erros);
        }

        if (nivel == null || nivel < 1) {
            erros.add("nivel deve ser maior ou igual a 1");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }
    }

    private void validarCompanheiro(CompanheiroRequest request) {
        List<String> erros = new ArrayList<>();

        if (request.getNome() == null || request.getNome().isBlank()) {
            erros.add("nome do companheiro é obrigatório");
        }

        if (request.getEspecie() == null || request.getEspecie().isBlank()) {
            erros.add("especie é obrigatória");
        } else {
            parseEspecie(request.getEspecie(), erros);
        }

        if (request.getLealdade() == null || request.getLealdade() < 0 || request.getLealdade() > 100) {
            erros.add("lealdade deve estar entre 0 e 100");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }
    }

    private void validarPaginacao(Integer page, Integer size) {
        List<String> erros = new ArrayList<>();

        if (page < 0) {
            erros.add("page não pode ser negativo");
        }

        if (size < 1 || size > 50) {
            erros.add("size deve estar entre 1 e 50");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }
    }

    private ClasseAventureiro parseClasse(String valor, List<String> erros) {
        try {
            return ClasseAventureiro.valueOf(valor);
        } catch (Exception e) {
            erros.add("classe inválida");
            return null;
        }
    }

    private EspecieCompanheiro parseEspecie(String valor, List<String> erros) {
        try {
            return EspecieCompanheiro.valueOf(valor);
        } catch (Exception e) {
            erros.add("especie inválida");
            return null;
        }
    }
}