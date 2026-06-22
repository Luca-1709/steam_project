package com.steam.ms_resenas;

import com.steam.ms_resenas.model.ResenaModel;
import com.steam.ms_resenas.repository.ResenaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ResenaRepository resenaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] titulos = {"Una obra maestra", "Decepcionante", "Muy recomendable",
                "Adictivo de principio a fin", "Esperaba más", "Mi juego del año",
                "Buena relación calidad-precio", "Lo volvería a comprar",
                "Regular tirando a bueno", "Imprescindible"};

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
                    || resenaRepository.existsByUsuarioIdAndJuegoId(usuarioId, juegoId)) {
                continue;
            }

            ResenaModel resena = new ResenaModel();
            resena.setUsuarioId(usuarioId);
            resena.setJuegoId(juegoId);
            resena.setPuntuacion(faker.number().numberBetween(1, 6));
            resena.setTitulo(faker.options().option(titulos));
            resena.setContenido(faker.lorem().paragraph());
            resena.setFecha(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
            resena.setActivo(true);

            resenaRepository.save(resena);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " resenas creadas");
    }
}
