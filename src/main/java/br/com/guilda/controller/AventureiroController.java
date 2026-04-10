package br.com.guilda.controller;

import br.com.guilda.dto.AventureiroCreateRequest;
import br.com.guilda.dto.AventureiroDetalheResponse;
import br.com.guilda.dto.AventureiroResumoResponse;
import br.com.guilda.dto.AventureiroUpdateRequest;
import br.com.guilda.dto.CompanheiroRequest;
import br.com.guilda.service.AventureiroService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guilda")
public class AventureiroController {

    private final AventureiroService service;

    public AventureiroController(AventureiroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AventureiroDetalheResponse> criar(@RequestBody AventureiroCreateRequest request) {
        AventureiroDetalheResponse response = service.criar(request);

        URI location = URI.create("/api/guilda/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AventureiroResumoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<AventureiroResumoResponse> lista;
        int total;

        if (nome != null && !nome.isBlank()) {
            lista = service.buscarPorNome(nome, page, size);
            total = service.contarPorNome(nome);
        } else {
            lista = service.listar(classe, ativo, nivelMinimo, page, size);
            total = service.contarFiltrados(classe, ativo, nivelMinimo);
        }

        int totalPages = (int) Math.ceil((double) total / size);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .header("X-Page", String.valueOf(page))
                .header("X-Size", String.valueOf(size))
                .header("X-Total-Pages", String.valueOf(totalPages))
                .body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventureiroDetalheResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AventureiroDetalheResponse> atualizar(
            @PathVariable Long id,
            @RequestBody AventureiroUpdateRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrar(@PathVariable Long id) {
        service.encerrarVinculo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recrutar")
    public ResponseEntity<Void> recrutar(@PathVariable Long id) {
        service.recrutarNovamente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/companheiro")
    public ResponseEntity<AventureiroDetalheResponse> definirCompanheiro(
            @PathVariable Long id,
            @RequestBody CompanheiroRequest request
    ) {
        return ResponseEntity.ok(service.definirCompanheiro(id, request));
    }

    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Void> removerCompanheiro(@PathVariable Long id) {
        service.removerCompanheiro(id);
        return ResponseEntity.noContent().build();
    }
}
