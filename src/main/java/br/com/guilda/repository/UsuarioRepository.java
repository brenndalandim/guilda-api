package br.com.guilda.repository;

import br.com.guilda.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
        select distinct u
        from Usuario u
        left join fetch u.organizacao
        left join fetch u.userRoles ur
        left join fetch ur.role r
        left join fetch r.rolePermissions rp
        left join fetch rp.permission
        where u.id = :id
    """)
    Optional<Usuario> buscarPorIdComRolesEPermissions(@Param("id") Long id);
    Optional<Usuario> findFirstByOrderByIdAsc();
}