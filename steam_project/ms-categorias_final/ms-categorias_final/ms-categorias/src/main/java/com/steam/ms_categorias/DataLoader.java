package com.steam.ms_categorias;

import com.steam.ms_categorias.model.Categoria;
import com.steam.ms_categorias.repository.CategoriaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] nombres = {"Acción", "Aventura", "RPG", "Estrategia", "Deportes",
                "Shooter", "Roguelike", "Simulación", "Terror", "Plataformas",
                "Carreras", "Puzzle"};

        int creadas = 0;
        for (String nombre : nombres) {
            // Restricción UNIQUE en 'nombre'
            if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
                continue;
            }

            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(faker.lorem().sentence(10));
            categoria.setActivo(true);

            categoriaRepository.save(categoria);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " categorias creadas");
    }
}