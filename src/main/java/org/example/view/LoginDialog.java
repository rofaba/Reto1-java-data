package org.example.view;

import org.example.infra.AuthProvider;
import org.example.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        super(owner, "", true);
        buildUI();
    }

    public void setAuth(AuthProvider auth) { this.auth = auth; }
    public void setOnLoginSuccess(Consumer<Usuario> cb) { this.onLoginSuccess = cb; }

    private void buildUI() {
        txtEmail = new JTextField(25);
        txtPassword = new JPasswordField(25);
        btnLogin = new JButton("Entrar");
        btnCancel = new JButton("Cancelar");

        JPanel form = new JPanel(new GridLayout(0,1,8,8));
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Contraseña"));
        form.add(txtPassword);

        JLabel title = new JLabel("Catálogo de Películas", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        JLabel note = new JLabel("Introduce tus credenciales para iniciar sesión", SwingConstants.CENTER);
        note.setFont(note.getFont().deriveFont(Font.ITALIC, 12f));

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.CENTER);
        header.add(note, BorderLayout.SOUTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.add(btnCancel);
        actions.add(btnLogin);

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(new JLabel("* Campos obligatorios", SwingConstants.LEFT), BorderLayout.WEST);
        footer.add(actions, BorderLayout.EAST);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(24, 24, 24, 24));
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);

        pack();
        setMinimumSize(new Dimension(460, 300));
        setLocationRelativeTo(getOwner());
        setResizable(false);

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
