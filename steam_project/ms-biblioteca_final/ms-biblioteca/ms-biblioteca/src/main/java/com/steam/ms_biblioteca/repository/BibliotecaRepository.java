package com.steam.ms_biblioteca.repository;

import com.steam.ms_biblioteca.model.BibliotecaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BibliotecaRepository extends JpaRepository<BibliotecaModel, Integer> {

    List<BibliotecaModel> findByUsuarioId(Integer usuarioId);

    boolean existsByUsuarioIdAndJuegoId(Integer usuarioId, Integer juegoId);

    // Todos los usuarios que tienen un juego (útil para ms-logros, ms-resenas)
    List<BibliotecaModel> findByJuegoId(Integer juegoId);
}
