package br.com.guilda.repository;

import br.com.guilda.model.UserRole;
import br.com.guilda.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}