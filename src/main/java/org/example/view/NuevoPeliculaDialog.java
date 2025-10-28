package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    public FormData getResult() { return result; }
    /* Construye la UI del diálogo */
    private void buildUI() {
        JLabel title = new JLabel("Agregar Nueva Película");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        txtTitle    = new JTextField(25);
        txtYear     = new JTextField(8);
        txtDirector = new JTextField(25);
        txtImage    = new JTextField(25);
        txtGenre    = new JTextField(15);

        txtDesc = new JTextArea(4, 25); // menos alto
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setMargin(new Insets(6,6,6,6));
        JScrollPane descScroll = new JScrollPane(txtDesc);
        descScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Form a dos columnas
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 0;

        addRow(form, g, "Título",        txtTitle);
        addRow(form, g, "Año",           txtYear);
        addRow(form, g, "Director",      txtDirector);
        addRow(form, g, "Género",        txtGenre);
        addRow(form, g, "Imagen (URL)",  txtImage);

        // Descripción
        JLabel lblDesc = new JLabel("Descripción");
        GridBagConstraints l = (GridBagConstraints) g.clone();
        l.gridx = 0; l.gridwidth = 2; l.weightx = 1; l.fill = GridBagConstraints.HORIZONTAL;
        form.add(lblDesc, l);

        GridBagConstraints d = (GridBagConstraints) g.clone();
        d.gridy++; d.gridx = 0; d.gridwidth = 2; d.weightx = 1; d.fill = GridBagConstraints.BOTH;
        d.ipady = 40;
        form.add(descScroll, d);

        // Botones
        JButton btnOk = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.add(btnCancel);
        actions.add(btnOk);

        // Root
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.add(title, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);

        pack();
        setMinimumSize(new Dimension(540, 360));
        setResizable(false);
        setLocationRelativeTo(getOwner());

        // Listeners
        btnOk.addActionListener(e -> {
            if (txtTitle.getText().isBlank() || txtDirector.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Título, Director y Año son obligatorios");
                return;
            }
            result = new FormData(
                    txtTitle.getText().trim(),
                    txtYear.getText().trim(),
                    txtDirector.getText().trim(),
                    txtDesc.getText(),
                    txtGenre.getText().trim(),
                    txtImage.getText().trim()
            );
            dispose();
        });
        btnCancel.addActionListener(e -> { result = null; dispose(); });
        getRootPane().setDefaultButton(btnOk);
    }
    /* Añade una fila al formulario */
    private static void addRow(JPanel form, GridBagConstraints g, String label, JComponent field) {
        GridBagConstraints c1 = (GridBagConstraints) g.clone();
        c1.gridx = 0; c1.weightx = 0; c1.gridwidth = 1; c1.fill = GridBagConstraints.NONE;
        form.add(new JLabel(label), c1);

        GridBagConstraints c2 = (GridBagConstraints) g.clone();
        c2.gridx = 1; c2.weightx = 1; c2.gridwidth = 1; c2.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, c2);

        g.gridy++;
    }
}