package br.com.guilda.repository;

import br.com.guilda.dto.RankingParticipacaoResponse;
import br.com.guilda.model.ParticipacaoMissao;
import br.com.guilda.model.ParticipacaoMissaoId;

import java.time.OffsetDateTime;
import java.util.List;

import br.com.guilda.model.StatusMissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {

    long countByAventureiroId(Long aventureiroId);

    @Query("""
        select p
        from ParticipacaoMissao p
        join fetch p.missao m
        where p.aventureiro.id = :aventureiroId
        order by p.createdAt desc
    """)
    List<ParticipacaoMissao> buscarUltimasParticipacoesComMissao(@Param("aventureiroId") Long aventureiroId);

    @Query("""
    select p
    from ParticipacaoMissao p
    join fetch p.aventureiro a
    where p.missao.id = :missaoId
""")
    List<ParticipacaoMissao> buscarParticipantesComAventureiro(@Param("missaoId") Long missaoId);

    @Query("""
        select new br.com.guilda.dto.RankingParticipacaoResponse(
            a.id,
            a.nome,
            cast(a.classe as string),
            count(p),
            coalesce(sum(p.recompensaOuro), 0),
            sum(case when p.destaque = true then 1L else 0L end)
        )
        from ParticipacaoMissao p
        join p.aventureiro a
        join p.missao m
        group by a.id, a.nome, a.classe
        order by count(p) desc, coalesce(sum(p.recompensaOuro), 0) desc
    """)
    List<RankingParticipacaoResponse> gerarRankingParticipacao();

    @Query("""
        select new br.com.guilda.dto.RankingParticipacaoResponse(
            a.id,
            a.nome,
            cast(a.classe as string),
            count(p),
            coalesce(sum(p.recompensaOuro), 0),
            sum(case when p.destaque = true then 1L else 0L end)
        )
        from ParticipacaoMissao p
        join p.aventureiro a
        join p.missao m
        where p.createdAt >= :inicio
          and p.createdAt < :fim
        group by a.id, a.nome, a.classe
        order by count(p) desc, coalesce(sum(p.recompensaOuro), 0) desc
    """)
    List<RankingParticipacaoResponse> gerarRankingParticipacaoPorPeriodo(@Param("inicio") OffsetDateTime inicio,
                                                                         @Param("fim") OffsetDateTime fim);

    @Query("""
        select new br.com.guilda.dto.RankingParticipacaoResponse(
            a.id,
            a.nome,
            cast(a.classe as string),
            count(p),
            coalesce(sum(p.recompensaOuro), 0),
            sum(case when p.destaque = true then 1L else 0L end)
        )
        from ParticipacaoMissao p
        join p.aventureiro a
        join p.missao m
        where m.status = :status
        group by a.id, a.nome, a.classe
        order by count(p) desc, coalesce(sum(p.recompensaOuro), 0) desc
    """)
    List<RankingParticipacaoResponse> gerarRankingParticipacaoPorStatus(@Param("status") StatusMissao status);

    @Query("""
        select new br.com.guilda.dto.RankingParticipacaoResponse(
            a.id,
            a.nome,
            cast(a.classe as string),
            count(p),
            coalesce(sum(p.recompensaOuro), 0),
            sum(case when p.destaque = true then 1L else 0L end)
        )
        from ParticipacaoMissao p
        join p.aventureiro a
        join p.missao m
        where p.createdAt >= :inicio
          and p.createdAt < :fim
          and m.status = :status
        group by a.id, a.nome, a.classe
        order by count(p) desc, coalesce(sum(p.recompensaOuro), 0) desc
    """)
    List<RankingParticipacaoResponse> gerarRankingParticipacaoPorPeriodoEStatus(@Param("inicio") OffsetDateTime inicio,
                                                                                @Param("fim") OffsetDateTime fim,
                                                                                @Param("status") StatusMissao status);
}