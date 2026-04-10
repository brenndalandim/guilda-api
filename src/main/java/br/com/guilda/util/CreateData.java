package br.com.guilda.util;

import br.com.guilda.model.Aventureiro;
import br.com.guilda.model.ClasseAventureiro;
import br.com.guilda.model.Companheiro;
import br.com.guilda.model.EspecieCompanheiro;
import br.com.guilda.repository.AventureiroRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Não é mais utilizado a partir do TP2, mas mantive para histórico
@Component
public class CreateData implements CommandLineRunner {
    private final AventureiroRepository repository;

    public CreateData(AventureiroRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        List<Aventureiro> lista = new ArrayList<>();
        ClasseAventureiro[] classes = ClasseAventureiro.values();

        for (long i = 1; i <= 100; i++) {
            Aventureiro a = new Aventureiro(
                    i,
                    "Aventureiro " + i,
                    classes[(int) ((i - 1) % classes.length)],
                    (int) ((i % 20) + 1),
                    i % 7 != 0
            );

            if (i % 4 == 0) {
                a.setCompanheiro(new Companheiro(
                        "Companheiro " + i,
                        EspecieCompanheiro.values()[(int) ((i - 1) % EspecieCompanheiro.values().length)],
                        (int) (50 + (i % 51))
                ));
            }

            lista.add(a);
        }

        //repository.seed(lista);
    }
}
