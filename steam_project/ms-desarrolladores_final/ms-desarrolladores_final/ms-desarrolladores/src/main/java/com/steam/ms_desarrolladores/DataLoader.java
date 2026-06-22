package com.steam.ms_desarrolladores;

import com.steam.ms_desarrolladores.model.Desarrollador;
import com.steam.ms_desarrolladores.repository.DesarrolladorRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] nombres = {"Supergiant Games", "CD Projekt Red", "FromSoftware",
                "Valve", "Rockstar Games", "Naughty Dog", "Bethesda",
                "Ubisoft", "Larian Studios", "Insomniac Games",
                "Santa Monica Studio", "id Software"};

        int creadas = 0;
        for (String nombre : nombres) {
            // Restricción UNIQUE en 'nombre'
            if (desarrolladorRepository.existsByNombreIgnoreCase(nombre)) {
                continue;
            }

            Desarrollador desarrollador = new Desarrollador();
            desarrollador.setNombre(nombre);
            desarrollador.setPais(faker.country().name());
            desarrollador.setDescripcion(faker.lorem().sentence(12));
            desarrollador.setSitioWeb("https://www."
                    + nombre.toLowerCase().replaceAll("[^a-z0-9]", "") + ".com");
            desarrollador.setActivo(true);

            desarrolladorRepository.save(desarrollador);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " desarrolladores creados");
    }
}
