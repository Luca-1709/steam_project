package com.steam.ms_logros;

import com.steam.ms_logros.model.Logro;
import com.steam.ms_logros.model.UsuarioLogro;
import com.steam.ms_logros.repository.LogroRepository;
import com.steam.ms_logros.repository.UsuarioLogroRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private LogroRepository logroRepository;
    @Autowired private UsuarioLogroRepository usuarioLogroRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] nombres = {"Primera victoria", "Maestro del sigilo", "Coleccionista",
                "Imparable", "Explorador", "Veterano", "Leyenda viviente",
                "Cazatesoros", "Superviviente", "Perfeccionista"};
        String[] condiciones = {"Completar el tutorial", "Ganar 10 partidas",
                "Reunir 100 monedas", "Alcanzar el nivel 50", "Derrotar al jefe final",
                "Jugar 100 horas", "Completar el juego al 100%",
                "Encontrar todos los secretos", "No recibir daño en una misión",
                "Conseguir todos los objetos"};

        int maxJuegoId = 5;     // coincide con el seed de ms-juegos
        int maxUsuarioId = 3;   // coincide con el seed de ms-usuarios

        // 1) Logros
        List<Integer> logroIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Logro logro = new Logro();
            logro.setNombre(faker.options().option(nombres) + " " + i);
            logro.setDescripcion(faker.lorem().sentence(10));
            logro.setJuegoId(faker.number().numberBetween(1, maxJuegoId + 1));
            logro.setCondicion(faker.options().option(condiciones));
            logroIds.add(logroRepository.save(logro).getId());
        }

        // 2) Logros desbloqueados por usuarios (UNIQUE usuario_id, logro_id)
        Set<String> generados = new HashSet<>();
        int desbloqueados = 0;
        int intentos = 0;
        while (desbloqueados < 15 && intentos < 80) {
            intentos++;
            int usuarioId = faker.number().numberBetween(1, maxUsuarioId + 1);
            int logroId = logroIds.get(faker.number().numberBetween(0, logroIds.size()));
            String par = usuarioId + "-" + logroId;

            if (!generados.add(par)
                    || usuarioLogroRepository.existsByUsuarioIdAndLogroId(usuarioId, logroId)) {
                continue;
            }

            UsuarioLogro ul = new UsuarioLogro();
            ul.setUsuarioId(usuarioId);
            ul.setLogroId(logroId);
            ul.setFechaLogro(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 200)));
            usuarioLogroRepository.save(ul);
            desbloqueados++;
        }

        System.out.println("✅ DataLoader: " + logroIds.size() + " logros y "
                + desbloqueados + " logros desbloqueados creados");
    }
}
