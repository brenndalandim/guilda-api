package br.com.guilda.repository;

import br.com.guilda.model.Missao;
import br.com.guilda.model.NivelPerigo;
import br.com.guilda.model.StatusMissao;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.guilda.dto.RelatorioMissaoResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    Page<Missao> findByStatus(StatusMissao status, Pageable pageable);

    Page<Missao> findByNivelPerigo(NivelPerigo nivelPerigo, Pageable pageable);

    Page<Missao> findByCreatedAtBetween(OffsetDateTime inicio, OffsetDateTime fim, Pageable pageable);

    Page<Missao> findByStatusAndNivelPerigo(StatusMissao status, NivelPerigo nivelPerigo, Pageable pageable);

    Page<Missao> findByStatusAndCreatedAtBetween(StatusMissao status, OffsetDateTime inicio, OffsetDateTime fim, Pageable pageable);

    Page<Missao> findByNivelPerigoAndCreatedAtBetween(NivelPerigo nivelPerigo, OffsetDateTime inicio, OffsetDateTime fim, Pageable pageable);

    Page<Missao> findByStatusAndNivelPerigoAndCreatedAtBetween(
            StatusMissao status,
            NivelPerigo nivelPerigo,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    );

    @Query("""
    select new br.com.guilda.dto.RelatorioMissaoResponse(
        m.id,
        m.titulo,
        cast(m.status as string),
        cast(m.nivelPerigo as string),
        count(p),
        coalesce(sum(p.recompensaOuro), 0)
    )
    from Missao m
    left join m.participacoes p
    group by m.id, m.titulo, m.status, m.nivelPerigo
    order by m.titulo asc
""")
    List<RelatorioMissaoResponse> gerarRelatorioMissoes();

    @Query("""
    select new br.com.guilda.dto.RelatorioMissaoResponse(
        m.id,
        m.titulo,
        cast(m.status as string),
        cast(m.nivelPerigo as string),
        count(p),
        coalesce(sum(p.recompensaOuro), 0)
    )
    from Missao m
    left join m.participacoes p
    where m.createdAt >= :inicio
      and m.createdAt < :fim
    group by m.id, m.titulo, m.status, m.nivelPerigo
    order by m.titulo asc
""")
    List<RelatorioMissaoResponse> gerarRelatorioMissoesPorPeriodo(@Param("inicio") OffsetDateTime inicio,
                                                                  @Param("fim") OffsetDateTime fim);
}