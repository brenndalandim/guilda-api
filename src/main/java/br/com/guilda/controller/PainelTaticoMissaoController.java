package br.com.guilda.controller;

import br.com.guilda.model.PainelTaticoMissao;
import br.com.guilda.service.PainelTaticoMissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PainelTaticoMissaoController {

    private final PainelTaticoMissaoService service;

    public PainelTaticoMissaoController(PainelTaticoMissaoService service) {
        this.service = service;
    }

    @GetMapping("/missoes/top15dias")
    public ResponseEntity<List<PainelTaticoMissao>> buscarTop15Dias() {
        return ResponseEntity.ok(service.buscarTopMissoesUltimos15Dias());
    }
}