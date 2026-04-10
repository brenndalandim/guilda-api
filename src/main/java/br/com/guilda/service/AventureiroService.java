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
import br.com.guilda.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import br.com.guilda.dto.UltimaMissaoResponse;
import br.com.guilda.model.ParticipacaoMissao;
import br.com.guilda.repository.ParticipacaoMissaoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import br.com.guilda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AventureiroService {

    private final AventureiroRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipacaoMissaoRepository participacaoMissaoRepository;

    public AventureiroService(AventureiroRepository repository,
                              UsuarioRepository usuarioRepository,
                              ParticipacaoMissaoRepository participacaoMissaoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.participacaoMissaoRepository = participacaoMissaoRepository;
    }

    public AventureiroDetalheResponse criar(AventureiroCreateRequest request) {
        validarAventureiro(request.getNome(), request.getClasse(), request.getNivel());

        Usuario usuarioCadastro = usuarioRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Usuário de cadastro não encontrado"));

        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setOrganizacao(usuarioCadastro.getOrganizacao());
        aventureiro.setUsuarioCadastro(usuarioCadastro);
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

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        ClasseAventureiro classeEnum = null;
        if (classe != null && !classe.isBlank()) {
            List<String> erros = new ArrayList<>();
            classeEnum = parseClasse(classe, erros);
            if (!erros.isEmpty()) {
                throw new InvalidRequestException("Solicitação inválida", erros);
            }
        }

        Page<Aventureiro> resultado;

        if (ativo != null && classeEnum != null && nivelMinimo != null) {
            resultado = repository.findByAtivoAndClasseAndNivelGreaterThanEqual(ativo, classeEnum, nivelMinimo, pageable);
        } else if (ativo != null && classeEnum != null) {
            resultado = repository.findByAtivoAndClasse(ativo, classeEnum, pageable);
        } else if (ativo != null && nivelMinimo != null) {
            resultado = repository.findByAtivoAndNivelGreaterThanEqual(ativo, nivelMinimo, pageable);
        } else if (classeEnum != null && nivelMinimo != null) {
            resultado = repository.findByClasseAndNivelGreaterThanEqual(classeEnum, nivelMinimo, pageable);
        } else if (ativo != null) {
            resultado = repository.findByAtivo(ativo, pageable);
        } else if (classeEnum != null) {
            resultado = repository.findByClasse(classeEnum, pageable);
        } else if (nivelMinimo != null) {
            resultado = repository.findByNivelGreaterThanEqual(nivelMinimo, pageable);
        } else {
            resultado = repository.findAll(pageable);
        }

        return resultado.getContent().stream()
                .map(this::toResumoResponse)
                .toList();
    }

    public List<AventureiroResumoResponse> buscarPorNome(String nome, Integer page, Integer size) {
        validarPaginacao(page, size);

        if (nome == null || nome.isBlank()) {
            throw new InvalidRequestException("Solicitação inválida", List.of("nome é obrigatório para busca"));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        return repository.findByNomeContainingIgnoreCase(nome.trim(), pageable)
                .getContent()
                .stream()
                .map(this::toResumoResponse)
                .toList();
    }

    public int contarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new InvalidRequestException("Solicitação inválida", List.of("nome é obrigatório para busca"));
        }

        Pageable pageable = PageRequest.of(0, 1);
        return (int) repository.findByNomeContainingIgnoreCase(nome.trim(), pageable).getTotalElements();
    }

    public int contarFiltrados(String classe, Boolean ativo, Integer nivelMinimo) {
        ClasseAventureiro classeEnum = null;

        if (classe != null && !classe.isBlank()) {
            List<String> erros = new ArrayList<>();
            classeEnum = parseClasse(classe, erros);
            if (!erros.isEmpty()) {
                throw new InvalidRequestException("Solicitação inválida", erros);
            }
        }

        Pageable pageable = PageRequest.of(0, 1);

        if (ativo != null && classeEnum != null && nivelMinimo != null) {
            return (int) repository.findByAtivoAndClasseAndNivelGreaterThanEqual(ativo, classeEnum, nivelMinimo, pageable).getTotalElements();
        } else if (ativo != null && classeEnum != null) {
            return (int) repository.findByAtivoAndClasse(ativo, classeEnum, pageable).getTotalElements();
        } else if (ativo != null && nivelMinimo != null) {
            return (int) repository.findByAtivoAndNivelGreaterThanEqual(ativo, nivelMinimo, pageable).getTotalElements();
        } else if (classeEnum != null && nivelMinimo != null) {
            return (int) repository.findByClasseAndNivelGreaterThanEqual(classeEnum, nivelMinimo, pageable).getTotalElements();
        } else if (ativo != null) {
            return (int) repository.findByAtivo(ativo, pageable).getTotalElements();
        } else if (classeEnum != null) {
            return (int) repository.findByClasse(classeEnum, pageable).getTotalElements();
        } else if (nivelMinimo != null) {
            return (int) repository.findByNivelGreaterThanEqual(nivelMinimo, pageable).getTotalElements();
        }

        return (int) repository.count();
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

        int totalParticipacoes = (int) participacaoMissaoRepository.countByAventureiroId(a.getId());

        UltimaMissaoResponse ultimaMissaoResponse = null;
        List<ParticipacaoMissao> participacoes = participacaoMissaoRepository.buscarUltimasParticipacoesComMissao(a.getId());

        if (!participacoes.isEmpty()) {
            var missao = participacoes.get(0).getMissao();
            ultimaMissaoResponse = new UltimaMissaoResponse(
                    missao.getId(),
                    missao.getTitulo(),
                    missao.getStatus().name(),
                    missao.getNivelPerigo().name()
            );
        }

        return new AventureiroDetalheResponse(
                a.getId(),
                a.getNome(),
                a.getClasse().name(),
                a.getNivel(),
                a.getAtivo(),
                companheiroResponse,
                totalParticipacoes,
                ultimaMissaoResponse
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