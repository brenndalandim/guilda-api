package br.com.guilda.controller;

import br.com.guilda.dto.AgrupamentoQuantidadeResponse;
import br.com.guilda.dto.FaixaPrecoResponse;
import br.com.guilda.dto.PrecoMedioResponse;
import br.com.guilda.service.ProdutoBuscaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos/agregacoes")
public class ProdutoAgregacaoController {

    private final ProdutoBuscaService service;

    public ProdutoAgregacaoController(ProdutoBuscaService service) {
        this.service = service;
    }

    @GetMapping("/por-categoria")
    public ResponseEntity<List<AgrupamentoQuantidadeResponse>> agruparPorCategoria() throws IOException {
        return ResponseEntity.ok(service.agruparPorCategoria());
    }

    @GetMapping("/por-raridade")
    public ResponseEntity<List<AgrupamentoQuantidadeResponse>> agruparPorRaridade() throws IOException {
        return ResponseEntity.ok(service.agruparPorRaridade());
    }

    @GetMapping("/preco-medio")
    public ResponseEntity<PrecoMedioResponse> calcularPrecoMedio() throws IOException {
        return ResponseEntity.ok(service.calcularPrecoMedio());
    }

    @GetMapping("/faixas-preco")
    public ResponseEntity<List<FaixaPrecoResponse>> agruparPorFaixaDePreco() throws IOException {
        return ResponseEntity.ok(service.agruparPorFaixaDePreco());
    }
}