package br.com.guilda.repository;

import br.com.guilda.model.Organizacao;
import br.com.guilda.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioPersistenciaTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Test
    void devePersistirUsuarioComOrganizacaoExistente() {
        Organizacao organizacao = organizacaoRepository.findById(1L).orElseThrow();

        Usuario usuario = new Usuario();
        usuario.setOrganizacao(organizacao);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario.teste." + System.currentTimeMillis() + "@guilda.com");
        usuario.setSenhaHash("hash-teste");
        usuario.setStatus("ATIVO");
        usuario.setCreatedAt(OffsetDateTime.now());
        usuario.setUpdatedAt(OffsetDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getOrganizacao()).isNotNull();
        assertThat(salvo.getOrganizacao().getId()).isEqualTo(organizacao.getId());
    }
}