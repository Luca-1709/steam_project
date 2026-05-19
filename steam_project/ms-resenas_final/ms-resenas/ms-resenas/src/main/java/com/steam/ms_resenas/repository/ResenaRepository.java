package com.steam.ms_resenas.repository;

import com.steam.ms_resenas.model.ResenaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<ResenaModel, Integer> {

    List<ResenaModel> findByJuegoId(Integer juegoId);

    List<ResenaModel> findByUsuarioId(Integer usuarioId);

    boolean existsByUsuarioIdAndJuegoId(Integer usuarioId, Integer juegoId);
}
