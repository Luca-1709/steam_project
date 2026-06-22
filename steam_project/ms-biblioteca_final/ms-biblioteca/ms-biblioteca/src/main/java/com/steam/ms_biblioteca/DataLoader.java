package com.steam.ms_biblioteca;

import com.steam.ms_biblioteca.model.BibliotecaModel;
import com.steam.ms_biblioteca.repository.BibliotecaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        int maxUsuarioId = 3;   // coincide con el seed de ms-usuarios
        int maxJuegoId = 5;     // coincide con el seed de ms-juegos
        Set<String> generados = new HashSet<>();

        int creadas = 0;
        int intentos = 0;
        while (creadas < 10 && intentos < 50) {
            intentos++;
            int usuarioId = faker.number().numberBetween(1, maxUsuarioId + 1);
            int juegoId = faker.number().numberBetween(1, maxJuegoId + 1);
            String par = usuarioId + "-" + juegoId;

            // Restricción UNIQUE (usuario_id, juego_id)
            if (!generados.add(par)
                    || bibliotecaRepository.existsByUsuarioIdAndJuegoId(usuarioId, juegoId)) {
                continue;
            }

            BibliotecaModel entrada = new BibliotecaModel();
            entrada.setUsuarioId(usuarioId);
            entrada.setJuegoId(juegoId);
            entrada.setFechaAdquisicion(
                    LocalDate.now().minusDays(faker.number().numberBetween(1, 365)));
            entrada.setHorasJugadas(faker.number().numberBetween(0, 500));
            entrada.setActivo(true);

            bibliotecaRepository.save(entrada);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " entradas de biblioteca creadas");
    }
}
