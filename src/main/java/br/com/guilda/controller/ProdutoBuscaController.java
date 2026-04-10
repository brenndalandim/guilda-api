package br.com.guilda.controller;

import br.com.guilda.dto.ProdutoLojaResponse;
import br.com.guilda.service.ProdutoBuscaService;
import br.com.guilda.dto.AgrupamentoQuantidadeResponse;
import br.com.guilda.dto.FaixaPrecoResponse;
import br.com.guilda.dto.PrecoMedioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos/busca")
public class ProdutoBuscaController {

    private final ProdutoBuscaService service;

    public ProdutoBuscaController(ProdutoBuscaService service) {
        this.service = service;
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarPorNome(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(service.buscarPorNome(termo));
    }

    @GetMapping("/descricao")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarPorDescricao(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(service.buscarPorDescricao(termo));
    }

    @GetMapping("/frase")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarPorFrase(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(service.buscarPorFraseExata(termo));
    }

    @GetMapping("/fuzzy")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarFuzzy(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(service.buscarFuzzyPorNome(termo));
    }

    @GetMapping("/multicampos")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarMulticampos(@RequestParam String termo) throws IOException {
        return ResponseEntity.ok(service.buscarEmMultiplosCampos(termo));
    }

    @GetMapping("/com-filtro")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarComFiltro(@RequestParam String termo,
                                                                     @RequestParam String categoria) throws IOException {
        return ResponseEntity.ok(service.buscarPorDescricaoComCategoria(termo, categoria));
    }

    @GetMapping("/faixa-preco")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarPorFaixaDePreco(@RequestParam Double min,
                                                                           @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(service.buscarPorFaixaDePreco(min, max));
    }

    @GetMapping("/avancada")
    public ResponseEntity<List<ProdutoLojaResponse>> buscarAvancada(@RequestParam String categoria,
                                                                    @RequestParam String raridade,
                                                                    @RequestParam Double min,
                                                                    @RequestParam Double max) throws IOException {
        return ResponseEntity.ok(service.buscarAvancada(categoria, raridade, min, max));
    }

}