package com.steam.ms_biblioteca;

import com.steam.ms_biblioteca.model.BibliotecaModel;
import com.steam.ms_biblioteca.repository.BibliotecaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();


        int[] usuarioIds = {1, 2, 3, 4, 5};
        int[] juegoIds   = {1, 2, 3, 4, 5};

        int creados = 0;
        int intentos = 0;

        while (creados < 10 && intentos < 50) {
            intentos++;
            int usuarioId = usuarioIds[random.nextInt(usuarioIds.length)];
            int juegoId   = juegoIds[random.nextInt(juegoIds.length)];

            if (bibliotecaRepository.existsByUsuarioIdAndJuegoId(usuarioId, juegoId)) {
                continue;
            }

            BibliotecaModel entrada = BibliotecaModel.builder()
                    .usuarioId(usuarioId)
                    .juegoId(juegoId)
                    .fechaAdquirido(LocalDate.now().minusDays(faker.number().numberBetween(1, 365)))
                    .horasJugadas(faker.number().numberBetween(0, 500))
                    .build();

            bibliotecaRepository.save(entrada);
            creados++;
        }

        System.out.println("[DataLoader] ms-biblioteca: " + creados + " entradas generadas con DataFaker.");
    }
}