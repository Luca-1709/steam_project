package com.steam.ms_tienda.repository;

import com.steam.ms_tienda.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByUsuarioId(Integer usuarioId);

    List<Compra> findByJuegoId(Integer juegoId);

    boolean existsByUsuarioIdAndJuegoId(Integer usuarioId, Integer juegoId);

    List<Compra> findByUsuarioIdAndEstado(Integer usuarioId, Integer estado);
}
