package com.steam.ms_amigos;

import com.steam.ms_amigos.model.Amistad;
import com.steam.ms_amigos.repository.AmistadRepository;
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
    private AmistadRepository amistadRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] estados = {"PENDIENTE", "ACEPTADA", "RECHAZADA"};
        int maxUsuarioId = 6;   // rango de usuarios de prueba
        Set<String> generados = new HashSet<>();

        int creadas = 0;
        int intentos = 0;
        while (creadas < 12 && intentos < 80) {
            intentos++;
            int usuarioId = faker.number().numberBetween(1, maxUsuarioId + 1);
            int amigoId = faker.number().numberBetween(1, maxUsuarioId + 1);

            // Un usuario no se agrega a si mismo
            if (usuarioId == amigoId) {
                continue;
            }
            String par = usuarioId + "-" + amigoId;

            // Restricción UNIQUE (usuario_id, amigo_id)
            if (!generados.add(par)
                    || amistadRepository.existsByUsuarioIdAndAmigoId(usuarioId, amigoId)) {
                continue;
            }

            Amistad amistad = new Amistad();
            amistad.setUsuarioId(usuarioId);
            amistad.setAmigoId(amigoId);
            amistad.setFechaAmistad(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
            amistad.setEstado(faker.options().option(estados));

            amistadRepository.save(amistad);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " amistades creadas");
    }
}
