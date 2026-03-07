package br.com.guilda.repository;

import br.com.guilda.model.Aventureiro;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class AventureiroRepository {
    private final List<Aventureiro> banco = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Aventureiro> findAll() {
        return banco;
    }

    public Optional<Aventureiro> findById(Long id) {
        return banco.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public Aventureiro save(Aventureiro aventureiro) {
        if (aventureiro.getId() == null) {
            aventureiro.setId(sequence.getAndIncrement());
            banco.add(aventureiro);
        } else {
            deleteById(aventureiro.getId());
            banco.add(aventureiro);
        }

        banco.sort(Comparator.comparing(Aventureiro::getId));
        return aventureiro;
    }

    public void deleteById(Long id) {
        banco.removeIf(a -> a.getId().equals(id));
    }

    public Long nextId() {
        return sequence.getAndIncrement();
    }

    public void seed(List<Aventureiro> aventureiros) {
        banco.clear();
        banco.addAll(aventureiros);
        banco.sort(Comparator.comparing(Aventureiro::getId));
        long maxId = banco.stream().mapToLong(Aventureiro::getId).max().orElse(0L);
        sequence.set(maxId + 1);
    }
}
