package br.com.guilda.service;

import br.com.guilda.dto.MissaoDetalheResponse;
import br.com.guilda.dto.MissaoResumoResponse;
import br.com.guilda.dto.RankingParticipacaoResponse;
import br.com.guilda.dto.RelatorioMissaoResponse;
import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.ClasseAventureiro;
import br.com.guilda.model.Missao;
import br.com.guilda.model.NivelPerigo;
import br.com.guilda.model.Organizacao;
import br.com.guilda.model.PapelMissao;
import br.com.guilda.model.ParticipacaoMissao;
import br.com.guilda.model.ParticipacaoMissaoId;
import br.com.guilda.model.StatusMissao;
import br.com.guilda.model.Usuario;
import br.com.guilda.repository.AventureiroRepository;
import br.com.guilda.repository.MissaoRepository;
import br.com.guilda.repository.ParticipacaoMissaoRepository;
import br.com.guilda.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MissaoConsultaServiceTest {

    @Autowired
    private MissaoService missaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MissaoRepository missaoRepository;

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private ParticipacaoMissaoRepository participacaoMissaoRepository;

    @Test
    void deveListarMissoesComFiltros() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Missao alvo = criarMissao(organizacao, "Missao Filtro Planejada", NivelPerigo.ALTO, StatusMissao.PLANEJADA);
        criarMissao(organizacao, "Missao Filtro Cancelada", NivelPerigo.ALTO, StatusMissao.CANCELADA);
        criarMissao(organizacao, "Missao Filtro Baixo", NivelPerigo.BAIXO, StatusMissao.PLANEJADA);

        List<MissaoResumoResponse> resultado = missaoService.listar("PLANEJADA", "ALTO", null, null, 0, 10);

        assertThat(resultado).extracting(MissaoResumoResponse::getId).contains(alvo.getId());
        assertThat(resultado).allMatch(m -> m.getStatus().equals("PLANEJADA"));
        assertThat(resultado).allMatch(m -> m.getNivelPerigo().equals("ALTO"));
    }

    @Test
    void deveRetornarDetalhamentoDeMissaoComParticipantes() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro aventureiro = criarAventureiro(organizacao, usuario, "Participante Detalhe", ClasseAventureiro.GUERREIRO, 10, true);
        Missao missao = criarMissao(organizacao, "Missao Detalhada", NivelPerigo.MEDIO, StatusMissao.PLANEJADA);

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(new ParticipacaoMissaoId(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(PapelMissao.LIDER);
        participacao.setRecompensaOuro(new BigDecimal("200.00"));
        participacao.setDestaque(true);
        participacaoMissaoRepository.save(participacao);

        MissaoDetalheResponse detalhe = missaoService.buscarDetalhe(missao.getId());

        assertThat(detalhe.getId()).isEqualTo(missao.getId());
        assertThat(detalhe.getParticipantes()).hasSize(1);
        assertThat(detalhe.getParticipantes().get(0).getNome()).isEqualTo("Participante Detalhe");
        assertThat(detalhe.getParticipantes().get(0).getPapelMissao()).isEqualTo("LIDER");
        assertThat(detalhe.getParticipantes().get(0).getRecompensaOuro()).isEqualByComparingTo("200.00");
        assertThat(detalhe.getParticipantes().get(0).getDestaque()).isTrue();
    }

    @Test
    void deveGerarRankingDeParticipacao() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro a1 = criarAventureiro(organizacao, usuario, "Ranking Um", ClasseAventureiro.GUERREIRO, 10, true);
        Aventureiro a2 = criarAventureiro(organizacao, usuario, "Ranking Dois", ClasseAventureiro.MAGO, 8, true);

        Missao m1 = criarMissao(organizacao, "Ranking Missao 1", NivelPerigo.MEDIO, StatusMissao.PLANEJADA);
        Missao m2 = criarMissao(organizacao, "Ranking Missao 2", NivelPerigo.ALTO, StatusMissao.PLANEJADA);

        salvarParticipacao(m1, a1, PapelMissao.LIDER, "100.00", true);
        salvarParticipacao(m2, a1, PapelMissao.COMBATENTE, "50.00", false);
        salvarParticipacao(m1, a2, PapelMissao.SUPORTE, "30.00", false);

        List<RankingParticipacaoResponse> ranking = missaoService.gerarRankingParticipacao(null, null, null);

        assertThat(ranking).isNotEmpty();
        RankingParticipacaoResponse primeiro = ranking.get(0);

        assertThat(primeiro.getAventureiroId()).isEqualTo(a1.getId());
        assertThat(primeiro.getTotalParticipacoes()).isEqualTo(2);
        assertThat(primeiro.getTotalRecompensas()).isEqualByComparingTo("150.00");
        assertThat(primeiro.getTotalDestaques()).isEqualTo(1);
    }

    @Test
    void deveGerarRelatorioDeMissoesComMetricas() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro aventureiro = criarAventureiro(organizacao, usuario, "Participante Relatorio", ClasseAventureiro.ARQUEIRO, 9, true);
        Missao missao = criarMissao(organizacao, "Relatorio Missao", NivelPerigo.EXTREMO, StatusMissao.PLANEJADA);

        salvarParticipacao(missao, aventureiro, PapelMissao.EXPLORADOR, "80.00", true);

        List<RelatorioMissaoResponse> relatorio = missaoService.gerarRelatorioMissoes(null, null);

        RelatorioMissaoResponse linha = relatorio.stream()
                .filter(r -> r.getMissaoId().equals(missao.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(linha.getTitulo()).isEqualTo("Relatorio Missao");
        assertThat(linha.getStatus()).isEqualTo("PLANEJADA");
        assertThat(linha.getNivelPerigo()).isEqualTo("EXTREMO");
        assertThat(linha.getQuantidadeParticipantes()).isEqualTo(1);
        assertThat(linha.getTotalRecompensas()).isEqualByComparingTo("80.00");
    }

    private Missao criarMissao(Organizacao organizacao, String titulo, NivelPerigo nivelPerigo, StatusMissao status) {
        Missao missao = new Missao();
        missao.setOrganizacao(organizacao);
        missao.setTitulo(titulo);
        missao.setNivelPerigo(nivelPerigo);
        missao.setStatus(status);
        return missaoRepository.save(missao);
    }

    private Aventureiro criarAventureiro(Organizacao organizacao,
                                         Usuario usuario,
                                         String nome,
                                         ClasseAventureiro classe,
                                         Integer nivel,
                                         Boolean ativo) {
        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setOrganizacao(organizacao);
        aventureiro.setUsuarioCadastro(usuario);
        aventureiro.setNome(nome);
        aventureiro.setClasse(classe);
        aventureiro.setNivel(nivel);
        aventureiro.setAtivo(ativo);
        return aventureiroRepository.save(aventureiro);
    }

    private void salvarParticipacao(Missao missao,
                                    Aventureiro aventureiro,
                                    PapelMissao papelMissao,
                                    String recompensa,
                                    Boolean destaque) {
        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(new ParticipacaoMissaoId(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(papelMissao);
        participacao.setRecompensaOuro(new BigDecimal(recompensa));
        participacao.setDestaque(destaque);
        participacaoMissaoRepository.save(participacao);
    }
}