package com.steam.ms_pagos.repository;

import com.steam.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByUsuarioId(Integer usuarioId);

    List<Pago> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
}