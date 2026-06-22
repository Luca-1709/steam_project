package com.steam.ms_juegos;

import com.steam.ms_juegos.Model.Juego;
import com.steam.ms_juegos.Repository.JuegoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private JuegoRepository juegoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] generos = {"Acción", "Aventura", "RPG", "Estrategia", "Deportes",
                "Shooter", "Roguelike", "Simulación", "Terror", "Plataformas"};

        for (int i = 0; i < 10; i++) {
            String titulo = faker.videoGame().title() + " " + i;
            if (juegoRepository.existsByTituloIgnoreCase(titulo)) {
                continue;
            }

            Juego juego = new Juego();
            juego.setTitulo(titulo);
            juego.setDescripcion(faker.lorem().sentence(12));
            juego.setPrecio(BigDecimal.valueOf(faker.number().numberBetween(4990, 59990)));
            juego.setFechaLanzamiento(
                    LocalDate.now().minusDays(faker.number().numberBetween(30, 3650)));
            juego.setDesarrollador(faker.company().name());
            juego.setGenero(faker.options().option(generos));
            juego.setImagenUrl("https://picsum.photos/seed/juego" + i + "/300/400");
            juego.setStock(faker.number().numberBetween(0, 100));
            juego.setActivo(true);

            juegoRepository.save(juego);
        }

        System.out.println("✅ DataLoader: 10 juegos de prueba creados");
    }
}
