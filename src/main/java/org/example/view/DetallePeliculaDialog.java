package org.example.view;

import org.example.model.Pelicula;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/** Diálogo de detalle con imagen en panel EAST de ancho fijo. */
public class DetallePeliculaDialog extends JDialog {

    public DetallePeliculaDialog(Frame owner, Pelicula p) {
        super(owner, "Detalle", true);

        // ----- Título y director
        JLabel lblTitle = new JLabel(p.getTitle() + " (" + p.getYear() + ")");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
        JLabel lblDirector = new JLabel("Director: " + p.getDirector());

        // ----- Descripción
        JTextArea txtDesc = new JTextArea(p.getDescription());
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);

        // ----- Imagen (placeholder + carga asíncrona + escalado)
        JLabel lblImage = new JLabel("Cargando imagen…", SwingConstants.CENTER);

        JPanel imagePane = new JPanel(new BorderLayout());
        imagePane.setPreferredSize(new Dimension(260, 1));  // ancho fijo para EAST
        imagePane.add(new JScrollPane(lblImage), BorderLayout.CENTER);

        if (p.getImageUrl() != null && !p.getImageUrl().isBlank()) {
            new SwingWorker<ImageIcon, Void>() {
                @Override protected ImageIcon doInBackground() throws Exception {
                    String urlStr = p.getImageUrl();
                    if (urlStr == null || urlStr.isBlank()) return null;

                    java.net.URL url = new java.net.URL(urlStr);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setInstanceFollowRedirects(true);
                    conn.setConnectTimeout(4000);
                    conn.setReadTimeout(4000);
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setRequestProperty("Referer", "https://example.com/"); // algunos sitios lo exigen
                    int code = conn.getResponseCode();
                    if (code >= 200 && code < 300) {
                        try (var in = new java.io.BufferedInputStream(conn.getInputStream())) {
                            var img = javax.imageio.ImageIO.read(in);
                            if (img == null) return null;
                            int w = 240, h = img.getHeight() * w / img.getWidth();
                            var scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
                            return new ImageIcon(scaled);
                        }
                    } else {
                        System.out.println("IMG HTTP " + code + " para " + urlStr);
                        return null;
                    }
                }
                @Override protected void done() {
                    try {
                        var icon = get();
                        lblImage.setIcon(icon);
                        lblImage.setText(icon == null ? "[No se pudo cargar imagen]" : "");
                    } catch (Exception e) {
                        lblImage.setText("[No se pudo cargar imagen]");
                    }
                    lblImage.revalidate(); lblImage.repaint();
                }
            }.execute();
        } else {
            lblImage.setText("[Sin imagen]");
        }

        // ----- Layout principal
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(lblTitle, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(lblDirector, BorderLayout.NORTH);
        center.add(new JScrollPane(txtDesc), BorderLayout.CENTER);

        content.add(center, BorderLayout.CENTER);
        content.add(imagePane, BorderLayout.EAST);

        JButton btnClose = new JButton("Cerrar");
        btnClose.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(btnClose);
        content.add(south, BorderLayout.SOUTH);

        setContentPane(content);
        setMinimumSize(new Dimension(720, 420));
        pack();
        setLocationRelativeTo(owner);
    }
}
