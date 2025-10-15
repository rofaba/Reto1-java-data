package org.example.infra;

import org.example.model.Usuario;
import java.io.IOException;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByEmail(String email) throws IOException;
    Optional<Usuario> findById(String id) throws IOException;
}
