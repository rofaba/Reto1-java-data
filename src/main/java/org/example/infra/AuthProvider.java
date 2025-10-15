package org.example.infra;
import org.example.model.Usuario;

import java.io.IOException;

public interface AuthProvider {
    /** @return usuario si las credenciales son válidas; null si no */
    Usuario login(String email, String password) throws IOException;

    void logout();

    Usuario getCurrentUser();

    boolean isLoggedIn();
}
