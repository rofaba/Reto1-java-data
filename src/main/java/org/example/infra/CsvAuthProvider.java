package org.example.infra;

import org.example.model.Usuario;

import java.io.IOException;
import java.util.Optional;

public class CsvAuthProvider implements AuthProvider {

    private final UsuarioRepository usuarios;
    private final SessionContext session;
/**
     * Crea un nuevo proveedor de autenticación basado en CSV.
     * @param usuarios repositorio de usuarios
     * @param session contexto de sesión
     */
    public CsvAuthProvider(UsuarioRepository usuarios, SessionContext session) {
        this.usuarios = usuarios;
        this.session = session;
    }
/**
     * Intenta autenticar un usuario con el email y contraseña proporcionados.
     * @param email email del usuario
     * @param password contraseña del usuario
     * @return el usuario autenticado si las credenciales son correctas, o null si no
     * @throws IOException si hay un error de E/S
     */
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
/**
     * Verifica si hay un usuario actualmente autenticado.
     * @return true si hay un usuario autenticado, false si no
     */
    @Override
    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }
}
