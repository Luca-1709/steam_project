package com.steam.ms_desarrolladores.repository;

import com.steam.ms_desarrolladores.model.Desarrollador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesarrolladorRepository extends JpaRepository<Desarrollador, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);
}