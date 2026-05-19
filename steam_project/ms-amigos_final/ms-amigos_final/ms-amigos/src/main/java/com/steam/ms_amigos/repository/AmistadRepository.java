package com.steam.ms_amigos.repository;

import com.steam.ms_amigos.model.Amistad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AmistadRepository extends JpaRepository<Amistad, Integer> {


    List<Amistad> findByUsuarioIdAndEstado(Integer usuarioId, String estado);

    List<Amistad> findByAmigoIdAndEstado(Integer amigoId, String estado);

    boolean existsByUsuarioIdAndAmigoId(Integer usuarioId, Integer amigoId);
}