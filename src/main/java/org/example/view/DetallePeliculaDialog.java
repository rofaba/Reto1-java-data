package org.example.view;

import org.example.model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Diálogo de detalle con imagen en panel EAST de ancho fijo y layout armónico. */
public class DetallePeliculaDialog extends JDialog {

    public DetallePeliculaDialog(Frame owner, Pelicula p) {
        super(owner, "Detalle", true);

        // Encabezado
        JLabel lblTitle = new JLabel(p.getTitle() + " (" + p.getYear() + ")", SwingConstants.LEFT);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));

        JLabel lblDirector = new JLabel("Director: " + p.getDirector());
        lblDirector.setFont(lblDirector.getFont().deriveFont(Font.PLAIN, 13f));

        JPanel header = new JPanel(new BorderLayout(4, 4));
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(lblDirector, BorderLayout.SOUTH);
        header.setBorder(new EmptyBorder(0, 0, 4, 0));


        // Descripción
        JTextArea txtDesc = new JTextArea(p.getDescription());
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setFont(txtDesc.getFont().deriveFont(13f));
        txtDesc.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane descScroll = new JScrollPane(txtDesc);
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        descScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Imagen
        JLabel lblImage = new JLabel("Cargando imagen…", SwingConstants.CENTER);
        lblImage.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane imageScroll = new JScrollPane(lblImage);
        imageScroll.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        imageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel imagePane = new JPanel(new BorderLayout());
        imagePane.setPreferredSize(new Dimension(260, 1)); // ancho fijo
        imagePane.add(imageScroll, BorderLayout.CENTER);
        imagePane.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Carga asíncrona segura
        if (p.getImageUrl() != null && !p.getImageUrl().isBlank()) {
            new SwingWorker<ImageIcon, Void>() {
                @Override protected ImageIcon doInBackground() throws Exception {
                    try {
                        var url = new java.net.URL(p.getImageUrl());
                        var conn = (java.net.HttpURLConnection) url.openConnection();
                        conn.setInstanceFollowRedirects(true);
                        conn.setConnectTimeout(4000);
                        conn.setReadTimeout(4000);
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        int code = conn.getResponseCode();
                        if (code >= 200 && code < 300) {
                            try (var in = new java.io.BufferedInputStream(conn.getInputStream())) {
                                var img = javax.imageio.ImageIO.read(in);
                                if (img == null) return null;
                                int w = 240, h = img.getHeight() * w / img.getWidth();
                                var scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                                return new ImageIcon(scaled);
                            }
                        }
                    } catch (Exception ignored) { }
                    return null;
                }
                @Override protected void done() {
                    try {
                        var icon = get();
                        lblImage.setIcon(icon);
                        lblImage.setText(icon == null ? "[No se pudo cargar imagen]" : "");
                    } catch (Exception ex) {
                        lblImage.setText("[No se pudo cargar imagen]");
                    }
                }
            }.execute();
        } else {
            lblImage.setText("[Sin imagen]");
        }

        // ----- Centro (solo texto)
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.add(descScroll, BorderLayout.CENTER);

        // ----- Botonera alineada a la derecha
        JButton btnClose = new JButton("Cerrar");
        btnClose.addActionListener(e -> dispose());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.add(btnClose);

        // ----- Panel raíz con padding general + gaps
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        content.add(header, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(imagePane, BorderLayout.EAST);
        content.add(actions, BorderLayout.SOUTH);

        setContentPane(content);

        //Tamaños mínimos y centrado
        setMinimumSize(new Dimension(760, 460));
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }
}
