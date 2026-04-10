package br.com.guilda.repository;

import br.com.guilda.model.PainelTaticoMissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PainelTaticoMissaoRepository extends JpaRepository<PainelTaticoMissao, Long> {

    List<PainelTaticoMissao> findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(
            LocalDateTime dataLimite
    );
}