package br.com.guilda.controller;

import br.com.guilda.dto.*;
import br.com.guilda.service.MissaoService;
import br.com.guilda.dto.RankingParticipacaoResponse;
import br.com.guilda.dto.RelatorioMissaoResponse;

import java.util.List;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/missoes")
public class MissaoController {

    private final MissaoService service;

    public MissaoController(MissaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MissaoResponse> criar(@RequestBody MissaoCreateRequest request) {
        MissaoResponse missao = service.criar(request);
        return ResponseEntity.created(URI.create("/api/missoes/" + missao.getId())).body(missao);
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<Void> adicionarParticipante(@PathVariable Long id,
                                                      @RequestBody ParticipacaoMissaoRequest request) {
        service.adicionarParticipante(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MissaoResumoResponse>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nivelPerigo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<MissaoResumoResponse> lista = service.listar(status, nivelPerigo, dataInicio, dataFim, page, size);
        int total = service.contarFiltradas(status, nivelPerigo, dataInicio, dataFim);
        int totalPages = (int) Math.ceil((double) total / size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .header("X-Page", String.valueOf(page))
                .header("X-Size", String.valueOf(size))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissaoDetalheResponse> buscarDetalhe(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarDetalhe(id));
    }

    @GetMapping("/relatorios/ranking-participacao")
    public ResponseEntity<List<RankingParticipacaoResponse>> gerarRankingParticipacao(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(service.gerarRankingParticipacao(dataInicio, dataFim, status));
    }

    @GetMapping("/relatorios/missoes-metricas")
    public ResponseEntity<List<RelatorioMissaoResponse>> gerarRelatorioMissoes(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim
    ) {
        return ResponseEntity.ok(service.gerarRelatorioMissoes(dataInicio, dataFim));
    }
}