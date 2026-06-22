package com.steam.ms_juegos;

import com.steam.ms_juegos.Model.Juego;
import com.steam.ms_juegos.Repository.JuegoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private JuegoRepository juegoRepository;
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] generos = {"RPG", "Accion", "Aventura", "Deportes", "Estrategia"};
        String[] desarrolladores = {"DarkStudio", "SpeedWorks", "PixelArts", "NovaSoft", "GameForge"};

        for (int i = 0; i < 10; i++) {
            Juego juego = new Juego();
            juego.setTitulo(faker.name().title() + i);
            juego.setGenero(generos[i % generos.length]);
            juego.setDesarrollador(desarrolladores[i % desarrolladores.length]);
            juego.setPrecio(BigDecimal.valueOf(faker.number().numberBetween(5000, 50000)));
            juego.setStock(faker.number().numberBetween(10, 100));
            juego.setActivo(true);

            juegoRepository.save(juego);
        }
        System.out.println("✅ DataLoader: 10 juegos de prueba creados");
    }
}