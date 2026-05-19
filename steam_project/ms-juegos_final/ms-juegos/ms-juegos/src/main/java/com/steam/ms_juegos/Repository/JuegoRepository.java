package com.steam.ms_juegos.Repository;

import com.steam.ms_juegos.Model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {

    List<Juego> findByGeneroIgnoreCase(String genero);

    List<Juego> findByActivoTrueAndStockGreaterThan(Integer stock);

    boolean existsByTituloIgnoreCase(String titulo);
}
