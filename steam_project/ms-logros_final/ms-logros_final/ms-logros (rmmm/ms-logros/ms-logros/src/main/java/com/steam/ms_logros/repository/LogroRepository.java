package com.steam.ms_logros.repository;

import com.steam.ms_logros.model.Logro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Integer> {

    List<Logro> findByJuegoId(Integer juegoId);
}