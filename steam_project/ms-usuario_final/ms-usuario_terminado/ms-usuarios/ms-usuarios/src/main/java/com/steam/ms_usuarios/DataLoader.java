package com.steam.ms_usuarios;

import com.steam.ms_usuarios.Model.UsuarioModel;
import com.steam.ms_usuarios.Repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            UsuarioModel usuario = new UsuarioModel();
            usuario.setNombre(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setUsername(faker.name().username() + i);
            usuario.setEmail(faker.internet().emailAddress() + i);
            usuario.setPassword(faker.internet().password());
            usuario.setPais(faker.address().country());
            usuario.setSaldo(BigDecimal.valueOf(faker.number().numberBetween(5000, 100000)));
            usuario.setFechaRegistro(LocalDate.now());
            usuario.setActivo(true);

            usuarioRepository.save(usuario);
        }

        System.out.println("✅ DataLoader: 10 usuarios de prueba creados");
    }
}
