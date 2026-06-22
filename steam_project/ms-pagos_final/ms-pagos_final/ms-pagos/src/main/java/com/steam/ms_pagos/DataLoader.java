package com.steam.ms_pagos;

import com.steam.ms_pagos.model.Pago;
import com.steam.ms_pagos.repository.PagoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        String[] metodos = {"TARJETA", "TRANSFERENCIA"};
        String[] estados = {"PENDIENTE", "COMPLETADO", "RECHAZADO"};
        String[] descripciones = {"Recarga de saldo", "Compra de saldo Steam",
                "Recarga con tarjeta", "Abono a billetera", "Recarga mensual"};

        int maxUsuarioId = 6;   // rango de usuarios de prueba

        for (int i = 0; i < 10; i++) {
            Pago pago = new Pago();
            pago.setUsuarioId(faker.number().numberBetween(1, maxUsuarioId + 1));
            pago.setMonto(BigDecimal.valueOf(faker.number().numberBetween(5000, 100000)));
            pago.setMetodoPago(faker.options().option(metodos));
            pago.setEstado(faker.options().option(estados));
            pago.setFechaPago(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)));
            pago.setDescripcion(faker.options().option(descripciones));

            pagoRepository.save(pago);
        }

        System.out.println("✅ DataLoader: 10 pagos de prueba creados");
    }
}