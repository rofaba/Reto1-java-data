package org.example.infra;

import org.example.model.Usuario;

import java.io.IOException;
import java.util.Optional;

public class CsvAuthProvider implements AuthProvider {

    private final UsuarioRepository usuarios;
    private final SessionContext session;

    public CsvAuthProvider(UsuarioRepository usuarios, SessionContext session) {
        this.usuarios = usuarios;
        this.session = session;
    }

    @Override
    public Usuario login(String email, String password) throws IOException {
        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password.trim();

        var u = usuarios.findByEmail(e);
        if (u.isPresent() && u.get().getPassword() != null
                && u.get().getPassword().trim().equals(p)) {
            session.setCurrentUser(u.get());
            return u.get();
        }
        return null;
    }


    @Override
    public void logout() {
        session.clear();
    }

    @Override
    public Usuario getCurrentUser() {
        return session.getCurrentUser();
    }

    @Override
    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }
}
