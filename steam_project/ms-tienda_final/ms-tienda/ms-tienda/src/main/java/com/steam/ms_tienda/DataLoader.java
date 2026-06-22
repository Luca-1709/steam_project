package com.steam.ms_tienda;

import com.steam.ms_tienda.model.Compra;
import com.steam.ms_tienda.repository.CompraRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] metodosPago = {"SALDO", "TARJETA", "PAYPAL", "TRANSFERENCIA"};
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

            // Regla de negocio: un usuario no compra dos veces el mismo juego
            if (!generados.add(par)
                    || compraRepository.existsByUsuarioIdAndJuegoId(usuarioId, juegoId)) {
                continue;
            }

            Compra compra = new Compra();
            compra.setUsuarioId(usuarioId);
            compra.setJuegoId(juegoId);
            compra.setPrecioPagado(BigDecimal.valueOf(faker.number().numberBetween(4990, 59990)));
            compra.setFechaCompra(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
            compra.setEstado("COMPLETADA");
            compra.setMetodoPago(faker.options().option(metodosPago));

            compraRepository.save(compra);
            creadas++;
        }

        System.out.println("✅ DataLoader: " + creadas + " compras de prueba creadas");
    }
}