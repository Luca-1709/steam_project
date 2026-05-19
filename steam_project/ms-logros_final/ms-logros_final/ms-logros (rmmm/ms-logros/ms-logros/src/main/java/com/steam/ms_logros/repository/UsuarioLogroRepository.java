package com.steam.ms_logros.repository;

import com.steam.ms_logros.model.UsuarioLogro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioLogroRepository extends JpaRepository<UsuarioLogro, Integer> {

    List<UsuarioLogro> findByUsuarioId(Integer usuarioId);

    boolean existsByUsuarioIdAndLogroId(Integer usuarioId, Integer logroId);
}