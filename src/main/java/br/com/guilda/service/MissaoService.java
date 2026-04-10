package br.com.guilda.service;

import br.com.guilda.dto.*;
import br.com.guilda.exception.InvalidRequestException;
import br.com.guilda.exception.ResourceNotFoundException;
import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.Missao;
import br.com.guilda.model.NivelPerigo;
import br.com.guilda.model.PapelMissao;
import br.com.guilda.model.ParticipacaoMissao;
import br.com.guilda.model.ParticipacaoMissaoId;
import br.com.guilda.model.StatusMissao;
import br.com.guilda.model.Usuario;
import br.com.guilda.repository.AventureiroRepository;
import br.com.guilda.repository.MissaoRepository;
import br.com.guilda.repository.ParticipacaoMissaoRepository;
import br.com.guilda.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final ParticipacaoMissaoRepository participacaoMissaoRepository;
    private final AventureiroRepository aventureiroRepository;
    private final UsuarioRepository usuarioRepository;

    public MissaoService(MissaoRepository missaoRepository,
                         ParticipacaoMissaoRepository participacaoMissaoRepository,
                         AventureiroRepository aventureiroRepository,
                         UsuarioRepository usuarioRepository) {
        this.missaoRepository = missaoRepository;
        this.participacaoMissaoRepository = participacaoMissaoRepository;
        this.aventureiroRepository = aventureiroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MissaoResponse criar(MissaoCreateRequest request) {
        validarMissao(request);

        Usuario usuarioCadastro = usuarioRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Usuário responsável não encontrado"));

        Missao missao = new Missao();
        missao.setOrganizacao(usuarioCadastro.getOrganizacao());
        missao.setTitulo(request.getTitulo().trim());
        missao.setNivelPerigo(NivelPerigo.valueOf(request.getNivelPerigo()));
        missao.setStatus(StatusMissao.PLANEJADA);

        Missao salva = missaoRepository.save(missao);
        return toResponse(salva);
    }

    @Transactional
    public void adicionarParticipante(Long missaoId, ParticipacaoMissaoRequest request) {
        validarParticipacao(request);

        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada"));

        Aventureiro aventureiro = aventureiroRepository.findById(request.getAventureiroId())
                .orElseThrow(() -> new ResourceNotFoundException("Aventureiro não encontrado"));

        if (!Boolean.TRUE.equals(aventureiro.getAtivo())) {
            throw new InvalidRequestException("Solicitação inválida", List.of("aventureiro inativo não pode ser associado a missão"));
        }

        if (!missao.getOrganizacao().getId().equals(aventureiro.getOrganizacao().getId())) {
            throw new InvalidRequestException("Solicitação inválida", List.of("aventureiro e missão devem pertencer à mesma organização"));
        }

        if (missao.getStatus() != StatusMissao.PLANEJADA && missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new InvalidRequestException("Solicitação inválida", List.of("missão não está em estado compatível para aceitar participantes"));
        }

        ParticipacaoMissaoId id = new ParticipacaoMissaoId(missao.getId(), aventureiro.getId());

        if (participacaoMissaoRepository.existsById(id)) {
            throw new InvalidRequestException("Solicitação inválida", List.of("aventureiro já participa dessa missão"));
        }

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(id);
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(PapelMissao.valueOf(request.getPapelMissao()));
        participacao.setRecompensaOuro(request.getRecompensaOuro());
        participacao.setDestaque(request.getDestaque());

        participacaoMissaoRepository.save(participacao);
    }

    public List<RelatorioMissaoResponse> gerarRelatorioMissoes(String dataInicio, String dataFim) {
        OffsetDateTime inicio = null;
        OffsetDateTime fim = null;

        List<String> erros = new ArrayList<>();

        if (dataInicio != null && !dataInicio.isBlank()) {
            try {
                inicio = java.time.LocalDate.parse(dataInicio).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataInicio inválida");
            }
        }

        if (dataFim != null && !dataFim.isBlank()) {
            try {
                fim = java.time.LocalDate.parse(dataFim).plusDays(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataFim inválida");
            }
        }

        if ((inicio == null && fim != null) || (inicio != null && fim == null)) {
            erros.add("dataInicio e dataFim devem ser informadas juntas");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }

        if (inicio != null) {
            return missaoRepository.gerarRelatorioMissoesPorPeriodo(inicio, fim);
        }

        return missaoRepository.gerarRelatorioMissoes();
    }

    public List<RankingParticipacaoResponse> gerarRankingParticipacao(String dataInicio,
                                                                      String dataFim,
                                                                      String status) {
        OffsetDateTime inicio = null;
        OffsetDateTime fim = null;
        StatusMissao statusEnum = null;

        List<String> erros = new ArrayList<>();

        if (dataInicio != null && !dataInicio.isBlank()) {
            try {
                inicio = java.time.LocalDate.parse(dataInicio).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataInicio inválida");
            }
        }

        if (dataFim != null && !dataFim.isBlank()) {
            try {
                fim = java.time.LocalDate.parse(dataFim).plusDays(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataFim inválida");
            }
        }

        if (status != null && !status.isBlank()) {
            try {
                statusEnum = StatusMissao.valueOf(status);
            } catch (Exception e) {
                erros.add("status inválido");
            }
        }

        if ((inicio == null && fim != null) || (inicio != null && fim == null)) {
            erros.add("dataInicio e dataFim devem ser informadas juntas");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }

        if (inicio != null && statusEnum != null) {
            return participacaoMissaoRepository.gerarRankingParticipacaoPorPeriodoEStatus(inicio, fim, statusEnum);
        } else if (inicio != null) {
            return participacaoMissaoRepository.gerarRankingParticipacaoPorPeriodo(inicio, fim);
        } else if (statusEnum != null) {
            return participacaoMissaoRepository.gerarRankingParticipacaoPorStatus(statusEnum);
        }

        return participacaoMissaoRepository.gerarRankingParticipacao();
    }

    public List<MissaoResumoResponse> listar(String status,
                                             String nivelPerigo,
                                             String dataInicio,
                                             String dataFim,
                                             Integer page,
                                             Integer size) {
        validarPaginacao(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo").ascending());

        StatusMissao statusEnum = null;
        NivelPerigo nivelPerigoEnum = null;
        OffsetDateTime inicio = null;
        OffsetDateTime fim = null;

        List<String> erros = new ArrayList<>();

        if (status != null && !status.isBlank()) {
            try {
                statusEnum = StatusMissao.valueOf(status);
            } catch (Exception e) {
                erros.add("status inválido");
            }
        }

        if (nivelPerigo != null && !nivelPerigo.isBlank()) {
            try {
                nivelPerigoEnum = NivelPerigo.valueOf(nivelPerigo);
            } catch (Exception e) {
                erros.add("nivelPerigo inválido");
            }
        }

        if (dataInicio != null && !dataInicio.isBlank()) {
            try {
                inicio = LocalDate.parse(dataInicio).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataInicio inválida");
            }
        }

        if (dataFim != null && !dataFim.isBlank()) {
            try {
                fim = LocalDate.parse(dataFim).plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataFim inválida");
            }
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }

        Page<Missao> resultado;

        if (statusEnum != null && nivelPerigoEnum != null && inicio != null && fim != null) {
            resultado = missaoRepository.findByStatusAndNivelPerigoAndCreatedAtBetween(statusEnum, nivelPerigoEnum, inicio, fim, pageable);
        } else if (statusEnum != null && nivelPerigoEnum != null) {
            resultado = missaoRepository.findByStatusAndNivelPerigo(statusEnum, nivelPerigoEnum, pageable);
        } else if (statusEnum != null && inicio != null && fim != null) {
            resultado = missaoRepository.findByStatusAndCreatedAtBetween(statusEnum, inicio, fim, pageable);
        } else if (nivelPerigoEnum != null && inicio != null && fim != null) {
            resultado = missaoRepository.findByNivelPerigoAndCreatedAtBetween(nivelPerigoEnum, inicio, fim, pageable);
        } else if (statusEnum != null) {
            resultado = missaoRepository.findByStatus(statusEnum, pageable);
        } else if (nivelPerigoEnum != null) {
            resultado = missaoRepository.findByNivelPerigo(nivelPerigoEnum, pageable);
        } else if (inicio != null && fim != null) {
            resultado = missaoRepository.findByCreatedAtBetween(inicio, fim, pageable);
        } else {
            resultado = missaoRepository.findAll(pageable);
        }

        return resultado.getContent().stream()
                .map(this::toResumoResponse)
                .toList();
    }

    public int contarFiltradas(String status,
                               String nivelPerigo,
                               String dataInicio,
                               String dataFim) {
        Pageable pageable = PageRequest.of(0, 1);

        StatusMissao statusEnum = null;
        NivelPerigo nivelPerigoEnum = null;
        OffsetDateTime inicio = null;
        OffsetDateTime fim = null;

        List<String> erros = new ArrayList<>();

        if (status != null && !status.isBlank()) {
            try {
                statusEnum = StatusMissao.valueOf(status);
            } catch (Exception e) {
                erros.add("status inválido");
            }
        }

        if (nivelPerigo != null && !nivelPerigo.isBlank()) {
            try {
                nivelPerigoEnum = NivelPerigo.valueOf(nivelPerigo);
            } catch (Exception e) {
                erros.add("nivelPerigo inválido");
            }
        }

        if (dataInicio != null && !dataInicio.isBlank()) {
            try {
                inicio = LocalDate.parse(dataInicio).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataInicio inválida");
            }
        }

        if (dataFim != null && !dataFim.isBlank()) {
            try {
                fim = LocalDate.parse(dataFim).plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (Exception e) {
                erros.add("dataFim inválida");
            }
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }

        if (statusEnum != null && nivelPerigoEnum != null && inicio != null && fim != null) {
            return (int) missaoRepository.findByStatusAndNivelPerigoAndCreatedAtBetween(statusEnum, nivelPerigoEnum, inicio, fim, pageable).getTotalElements();
        } else if (statusEnum != null && nivelPerigoEnum != null) {
            return (int) missaoRepository.findByStatusAndNivelPerigo(statusEnum, nivelPerigoEnum, pageable).getTotalElements();
        } else if (statusEnum != null && inicio != null && fim != null) {
            return (int) missaoRepository.findByStatusAndCreatedAtBetween(statusEnum, inicio, fim, pageable).getTotalElements();
        } else if (nivelPerigoEnum != null && inicio != null && fim != null) {
            return (int) missaoRepository.findByNivelPerigoAndCreatedAtBetween(nivelPerigoEnum, inicio, fim, pageable).getTotalElements();
        } else if (statusEnum != null) {
            return (int) missaoRepository.findByStatus(statusEnum, pageable).getTotalElements();
        } else if (nivelPerigoEnum != null) {
            return (int) missaoRepository.findByNivelPerigo(nivelPerigoEnum, pageable).getTotalElements();
        } else if (inicio != null && fim != null) {
            return (int) missaoRepository.findByCreatedAtBetween(inicio, fim, pageable).getTotalElements();
        }

        return (int) missaoRepository.count();
    }

    public MissaoDetalheResponse buscarDetalhe(Long id) {
        Missao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada"));

        List<ParticipacaoMissao> participacoes =
                participacaoMissaoRepository.buscarParticipantesComAventureiro(id);

        List<ParticipanteMissaoResponse> participantes = participacoes.stream()
                .map(p -> new ParticipanteMissaoResponse(
                        p.getAventureiro().getId(),
                        p.getAventureiro().getNome(),
                        p.getAventureiro().getClasse().name(),
                        p.getAventureiro().getNivel(),
                        p.getPapelMissao().name(),
                        p.getRecompensaOuro(),
                        p.getDestaque()
                ))
                .toList();

        return new MissaoDetalheResponse(
                missao.getId(),
                missao.getTitulo(),
                missao.getStatus().name(),
                missao.getNivelPerigo().name(),
                missao.getCreatedAt() != null ? missao.getCreatedAt().toString() : null,
                missao.getDataInicio() != null ? missao.getDataInicio().toString() : null,
                missao.getDataFim() != null ? missao.getDataFim().toString() : null,
                participantes
        );
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

    private MissaoResumoResponse toResumoResponse(Missao missao) {
        return new MissaoResumoResponse(
                missao.getId(),
                missao.getTitulo(),
                missao.getStatus().name(),
                missao.getNivelPerigo().name(),
                missao.getCreatedAt() != null ? missao.getCreatedAt().toString() : null,
                missao.getDataInicio() != null ? missao.getDataInicio().toString() : null,
                missao.getDataFim() != null ? missao.getDataFim().toString() : null
        );
    }

    private MissaoResponse toResponse(Missao missao) {
        return new MissaoResponse(
                missao.getId(),
                missao.getOrganizacao().getId(),
                missao.getTitulo(),
                missao.getNivelPerigo().name(),
                missao.getStatus().name(),
                missao.getCreatedAt() != null ? missao.getCreatedAt().toString() : null,
                missao.getDataInicio() != null ? missao.getDataInicio().toString() : null,
                missao.getDataFim() != null ? missao.getDataFim().toString() : null
        );
    }

    private void validarMissao(MissaoCreateRequest request) {
        List<String> erros = new ArrayList<>();

        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            erros.add("titulo é obrigatório");
        } else if (request.getTitulo().length() > 150) {
            erros.add("titulo deve ter no máximo 150 caracteres");
        }

        if (request.getNivelPerigo() == null || request.getNivelPerigo().isBlank()) {
            erros.add("nivelPerigo é obrigatório");
        } else {
            try {
                NivelPerigo.valueOf(request.getNivelPerigo());
            } catch (Exception e) {
                erros.add("nivelPerigo inválido");
            }
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }
    }

    private void validarParticipacao(ParticipacaoMissaoRequest request) {
        List<String> erros = new ArrayList<>();

        if (request.getAventureiroId() == null) {
            erros.add("aventureiroId é obrigatório");
        }

        if (request.getPapelMissao() == null || request.getPapelMissao().isBlank()) {
            erros.add("papelMissao é obrigatório");
        } else {
            try {
                PapelMissao.valueOf(request.getPapelMissao());
            } catch (Exception e) {
                erros.add("papelMissao inválido");
            }
        }

        if (request.getRecompensaOuro() != null && request.getRecompensaOuro().signum() < 0) {
            erros.add("recompensaOuro deve ser maior ou igual a zero");
        }

        if (request.getDestaque() == null) {
            erros.add("destaque é obrigatório");
        }

        if (!erros.isEmpty()) {
            throw new InvalidRequestException("Solicitação inválida", erros);
        }
    }
}