package br.com.guilda.repository;

import br.com.guilda.model.Role;
import br.com.guilda.model.RolePermission;
import br.com.guilda.model.UserRole;
import br.com.guilda.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveCarregarUsuarioComOrganizacaoRolesEPermissions() {
        Optional<Usuario> resultado = usuarioRepository.buscarPorIdComRolesEPermissions(1L);

        assertThat(resultado).isPresent();

        Usuario usuario = resultado.get();

        assertThat(usuario.getOrganizacao()).isNotNull();
        assertThat(usuario.getUserRoles()).isNotEmpty();

        List<Role> roles = usuario.getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .toList();

        assertThat(roles).isNotEmpty();

        long totalPermissions = roles.stream()
                .flatMap(role -> role.getRolePermissions().stream())
                .map(RolePermission::getPermission)
                .count();

        assertThat(totalPermissions).isGreaterThan(0);
    }
}