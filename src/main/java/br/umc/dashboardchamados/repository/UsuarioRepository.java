package br.umc.dashboardchamados.repository;

import br.umc.dashboardchamados.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
}
