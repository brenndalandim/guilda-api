package br.com.guilda.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", schema = "audit")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 80)
    private String code;

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions = new LinkedHashSet<>();

    public Permission() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<RolePermission> getRolePermissions() {
        return rolePermissions;
    }
}