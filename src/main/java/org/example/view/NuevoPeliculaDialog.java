package org.example.view;

import javax.swing.*;
import java.awt.*;

public class NuevoPeliculaDialog extends JDialog {

    public record FormData(String title, String year, String director, String description, String genre, String imageUrl) {}

    private JTextField txtTitle, txtYear, txtDirector, txtImage, txtGenre;
    private JTextArea txtDesc;
    private FormData result;

    public NuevoPeliculaDialog(Frame owner) {
        super(owner, "Nueva película", true);
        buildUI();
    }

    public FormData getResult() { return result; } // null si canceló

    private void buildUI() {
        txtTitle = new JTextField(25);
        txtYear = new JTextField(8);
        txtDirector = new JTextField(25);
        txtImage = new JTextField(25);
        txtDesc = new JTextArea(5, 25);
        txtGenre = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(0,1,6,6));
        form.add(new JLabel("Título")); form.add(txtTitle);
        form.add(new JLabel("Año")); form.add(txtYear);
        form.add(new JLabel("Director")); form.add(txtDirector);
        form.add(new JLabel("Imagen (URL)")); form.add(txtImage);
        form.add(new JLabel("Descripción")); form.add(txtDesc);
        form.add(new JLabel("Género")); form.add(txtGenre);


        JButton btnOk = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        JPanel actions = new JPanel();
        actions.add(btnCancel); actions.add(btnOk);

        setLayout(new BorderLayout(8,8));
        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());

        btnOk.addActionListener(e -> {
            if (txtTitle.getText().isBlank() || txtDirector.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Título y Director son obligatorios");
                return;
            }
            result = new FormData(
                    txtTitle.getText().trim(),
                    txtYear.getText().trim(),
                    txtDirector.getText().trim(),
                    txtDesc.getText(),
                    txtGenre.getText(),
                    txtImage.getText().trim()
            );
            dispose();
        });
        btnCancel.addActionListener(e -> { result = null; dispose(); });
        getRootPane().setDefaultButton(btnOk);
    }
}
