package br.com.guilda.service;

import br.com.guilda.model.PainelTaticoMissao;
import br.com.guilda.repository.PainelTaticoMissaoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PainelTaticoMissaoService {

    private final PainelTaticoMissaoRepository repository;

    public PainelTaticoMissaoService(PainelTaticoMissaoRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "topMissoes15Dias", key = "'rankingTatico'")
    public List<PainelTaticoMissao> buscarTopMissoesUltimos15Dias() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);
        return repository.findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(dataLimite);
    }
}