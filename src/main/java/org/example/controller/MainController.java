package org.example.controller;

import org.example.infra.AuthProvider;
import org.example.infra.SessionContext;
import org.example.infra.PeliculaRepository;
import org.example.view.LoginDialog;
import org.example.view.MainFrame;

import javax.swing.*;

public class MainController {

    private final PeliculaRepository peliculaRepo;
    private final AuthProvider auth;
    private final SessionContext session;

    private MainFrame mainFrame; // inyectado en init(...)

    public MainController(PeliculaRepository peliculaRepo, AuthProvider auth, SessionContext session) {
        this.peliculaRepo = peliculaRepo;
        this.auth = auth;
        this.session = session;
    }

    /** Inyecta MainFrame y arranca el flujo con Login (como tu estructura previa) */
    public void init(MainFrame frame) {
        this.mainFrame = frame;
        this.mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { doLogout(); }
            @Override public void windowClosing(java.awt.event.WindowEvent e) { doLogout(); }
        });
        System.out.println("[CONTROLLER] init -> showLogin");
        SwingUtilities.invokeLater(this::showLogin);
    }

    private void showLogin() {
        System.out.println("[CONTROLLER] creando LoginDialog");
        LoginDialog login = new LoginDialog(null);
        login.setAuth(auth);
        login.setOnLoginSuccess(u -> {
            System.out.println("[CONTROLLER] login OK para: " + u.getEmail());
            showMain();
        });
        login.setVisible(true); // modal; bloquea hasta cerrar
        System.out.println("[CONTROLLER] LoginDialog cerrado");
    }

    private void showMain() {
        if (mainFrame == null) {
            System.out.println("[CONTROLLER][WARN] mainFrame era null, creando fallback");
            mainFrame = new MainFrame(peliculaRepo, session);
            mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override public void windowClosed(java.awt.event.WindowEvent e) { doLogout(); }
                @Override public void windowClosing(java.awt.event.WindowEvent e) { doLogout(); }
            });
        }
        try {
            System.out.println("[CONTROLLER] loadData()");
            mainFrame.loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[CONTROLLER] mostrando MainFrame");
        mainFrame.setVisible(true);
    }

    private void doLogout() {
        try { auth.logout(); } catch (Exception ignored) {}
        if (mainFrame != null) mainFrame.setVisible(false);
        System.out.println("[CONTROLLER] logout -> showLogin");
        SwingUtilities.invokeLater(this::showLogin);
    }
}

