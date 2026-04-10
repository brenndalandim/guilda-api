package br.com.guilda.service;

import br.com.guilda.dto.AventureiroDetalheResponse;
import br.com.guilda.dto.AventureiroResumoResponse;
import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.ClasseAventureiro;
import br.com.guilda.model.Companheiro;
import br.com.guilda.model.EspecieCompanheiro;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AventureiroConsultaServiceTest {

    @Autowired
    private AventureiroService aventureiroService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private MissaoRepository missaoRepository;

    @Autowired
    private ParticipacaoMissaoRepository participacaoMissaoRepository;

    @Test
    void deveListarAventureirosComFiltros() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro a1 = criarAventureiro(organizacao, usuario, "Filtro Guerreiro Ativo", ClasseAventureiro.GUERREIRO, 10, true);
        criarAventureiro(organizacao, usuario, "Filtro Guerreiro Inativo", ClasseAventureiro.GUERREIRO, 10, false);
        criarAventureiro(organizacao, usuario, "Filtro Mago Ativo", ClasseAventureiro.MAGO, 10, true);
        criarAventureiro(organizacao, usuario, "Filtro Guerreiro Nivel Baixo", ClasseAventureiro.GUERREIRO, 1, true);

        List<AventureiroResumoResponse> resultado = aventureiroService.listar("GUERREIRO", true, 5, 0, 10);

        assertThat(resultado).extracting(AventureiroResumoResponse::getId).contains(a1.getId());
        assertThat(resultado).allMatch(a -> a.getClasse().equals("GUERREIRO"));
        assertThat(resultado).allMatch(AventureiroResumoResponse::getAtivo);
        assertThat(resultado).allMatch(a -> a.getNivel() >= 5);
    }

    @Test
    void deveBuscarAventureiroPorNomeParcial() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro alvo = criarAventureiro(organizacao, usuario, "Arthorius Busca Parcial", ClasseAventureiro.GUERREIRO, 8, true);
        criarAventureiro(organizacao, usuario, "Beltrano", ClasseAventureiro.MAGO, 5, true);

        List<AventureiroResumoResponse> resultado = aventureiroService.buscarPorNome("thor", 0, 10);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).extracting(AventureiroResumoResponse::getId).contains(alvo.getId());
        assertThat(resultado).allMatch(a -> a.getNome().toLowerCase().contains("thor"));
    }

    @Test
    void deveRetornarVisualizacaoCompletaDoAventureiro() {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElseThrow();
        Organizacao organizacao = usuario.getOrganizacao();

        Aventureiro aventureiro = criarAventureiro(organizacao, usuario, "Detalhe Completo", ClasseAventureiro.ARQUEIRO, 12, true);
        aventureiro.setCompanheiro(new Companheiro("Fenrir Teste", EspecieCompanheiro.LOBO, 90));
        aventureiro = aventureiroRepository.save(aventureiro);

        Missao missao = new Missao();
        missao.setOrganizacao(organizacao);
        missao.setTitulo("Missao do Detalhe");
        missao.setNivelPerigo(NivelPerigo.MEDIO);
        missao.setStatus(StatusMissao.PLANEJADA);
        missao = missaoRepository.save(missao);

        ParticipacaoMissao participacao = new ParticipacaoMissao();
        participacao.setId(new ParticipacaoMissaoId(missao.getId(), aventureiro.getId()));
        participacao.setMissao(missao);
        participacao.setAventureiro(aventureiro);
        participacao.setPapelMissao(PapelMissao.LIDER);
        participacao.setRecompensaOuro(new BigDecimal("150.00"));
        participacao.setDestaque(true);
        participacaoMissaoRepository.save(participacao);

        AventureiroDetalheResponse detalhe = aventureiroService.buscarPorId(aventureiro.getId());

        assertThat(detalhe.getId()).isEqualTo(aventureiro.getId());
        assertThat(detalhe.getCompanheiro()).isNotNull();
        assertThat(detalhe.getCompanheiro().getNome()).isEqualTo("Fenrir Teste");
        assertThat(detalhe.getTotalParticipacoes()).isEqualTo(1);
        assertThat(detalhe.getUltimaMissao()).isNotNull();
        assertThat(detalhe.getUltimaMissao().getTitulo()).isEqualTo("Missao do Detalhe");
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
}