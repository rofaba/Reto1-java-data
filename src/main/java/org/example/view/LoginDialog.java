package org.example.view;

import org.example.infra.AuthProvider;
import org.example.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class LoginDialog extends JDialog {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    private AuthProvider auth;
    private Consumer<Usuario> onLoginSuccess;

    public LoginDialog(Frame owner) {
        super(owner, "Login", true);
        buildUI();
    }

    public void setAuth(AuthProvider auth) { this.auth = auth; }
    public void setOnLoginSuccess(Consumer<Usuario> cb) { this.onLoginSuccess = cb; }

    private void buildUI() {
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Entrar");
        btnCancel = new JButton("Cancelar");

        JPanel form = new JPanel(new GridLayout(0,1,6,6));
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Contraseña"));
        form.add(txtPassword);

        JPanel actions = new JPanel();
        actions.add(btnCancel);
        actions.add(btnLogin);

        setLayout(new BorderLayout(8,8));
        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());

        btnLogin.addActionListener(e -> doLogin());
        btnCancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnLogin);
    }

    private void doLogin() {
        String email = txtEmail.getText().trim();
        String pass  = new String(txtPassword.getPassword()).trim();
        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa email y contraseña");
            return;
        }
        try {
            var u = auth.login(email, pass);
            if (u != null) {
                if (onLoginSuccess != null) onLoginSuccess.accept(u);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error leyendo usuarios:\n" + ex.getMessage());
        }
    }
}
