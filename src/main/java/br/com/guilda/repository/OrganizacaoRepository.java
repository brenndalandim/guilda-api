package br.com.guilda.repository;

import br.com.guilda.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}