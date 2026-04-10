package br.com.guilda.repository;

import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.ClasseAventureiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {

    Page<Aventureiro> findByAtivo(Boolean ativo, Pageable pageable);

    Page<Aventureiro> findByClasse(ClasseAventureiro classe, Pageable pageable);

    Page<Aventureiro> findByNivelGreaterThanEqual(Integer nivel, Pageable pageable);

    Page<Aventureiro> findByAtivoAndClasse(Boolean ativo, ClasseAventureiro classe, Pageable pageable);

    Page<Aventureiro> findByAtivoAndNivelGreaterThanEqual(Boolean ativo, Integer nivel, Pageable pageable);

    Page<Aventureiro> findByClasseAndNivelGreaterThanEqual(ClasseAventureiro classe, Integer nivel, Pageable pageable);

    Page<Aventureiro> findByAtivoAndClasseAndNivelGreaterThanEqual(Boolean ativo, ClasseAventureiro classe, Integer nivel, Pageable pageable);

    Page<Aventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}