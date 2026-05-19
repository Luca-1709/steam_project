package com.steam.ms_usuarios.Repository;
import com.steam.ms_usuarios.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    Optional<UsuarioModel> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}