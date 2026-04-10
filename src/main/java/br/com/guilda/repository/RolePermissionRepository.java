package br.com.guilda.repository;

import br.com.guilda.model.RolePermission;
import br.com.guilda.model.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}