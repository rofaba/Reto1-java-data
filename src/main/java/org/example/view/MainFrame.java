package org.example.view;

import org.example.infra.SessionContext;
import org.example.infra.PeliculaRepository;
import org.example.model.Pelicula;
import org.example.model.PeliculaTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainFrame extends JFrame {
    private final PeliculaRepository repo;
    private final SessionContext session;
    private JLabel lblUser;
    private JTable table;
    private PeliculaTableModel model;
    private JButton btnAdd, btnDelete, btnDetail, btnLogout;

    public MainFrame(PeliculaRepository repo, SessionContext session) {
        super("Mis Películas");
        this.repo = repo;
        this.session = session;
        buildUI();
    }

    private void buildUI() {
        table = new JTable();
        model = new PeliculaTableModel(new ArrayList<>());
        table.setModel(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setIntercellSpacing(new Dimension(4, 4));

        btnAdd = new JButton("Añadir");
        btnDelete = new JButton("Eliminar");
        btnDetail = new JButton("Detalles");
        btnLogout = new JButton("Cerrar sesión");

        // Gaps más amplios
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        top.add(btnAdd); top.add(btnDelete); top.add(btnDetail);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        lblUser = new JLabel();
        right.add(lblUser);
        right.add(btnLogout);

        JPanel north = new JPanel(new BorderLayout());
        north.setBorder(new javax.swing.border.EmptyBorder(6, 6, 6, 6)); // padding superior
        north.add(top, BorderLayout.WEST);
        north.add(right, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new javax.swing.border.EmptyBorder(4, 0, 0, 0));

        // Panel base layout general
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new javax.swing.border.EmptyBorder(16, 16, 16, 16)); // padding exterior
        root.add(north, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Listeners
        btnAdd.addActionListener(e -> onAdd());
        btnDelete.addActionListener(e -> onDelete());
        btnDetail.addActionListener(e -> onDetail());
        btnLogout.addActionListener(e -> onLogout());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) onDetail();
            }
        });
        updateUserLabel();
    }

    public void loadData() {
        try {
            var u = (session != null) ? session.getCurrentUser() : null;
            String userId = (u != null) ? u.getId() : null;

            List<Pelicula> pelis = (userId != null) ? repo.findAllByUser(userId) : List.of();
            model = new PeliculaTableModel(pelis);
            table.setModel(model);

            updateUserLabel();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el listado:\n"+e.getMessage());
        }
    }

    private void onAdd() {
        var dlg = new NuevoPeliculaDialog(this);
        dlg.setVisible(true);
        var data = dlg.getResult(); // puede ser null si canceló
        if (data == null) return;

        try {
            Pelicula p = new Pelicula(
                    UUID.randomUUID().toString(),
                    data.title(),
                    Integer.parseInt(data.year()),
                    data.director(),
                    data.description(),
                    data.genre(),
                    data.imageUrl(),
                    session.getCurrentUser().getId()
            );
            repo.add(p);
            loadData();
            JOptionPane.showMessageDialog(this, "Película añadida.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Año inválido.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al añadir:\n"+ex.getMessage());
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona una fila"); return; }
        Pelicula p = model.getAt(row);
        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar \""+p.getTitle()+"\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            boolean removed = repo.deleteById(p.getId(), session.getCurrentUser().getId());
            if (removed) { loadData(); JOptionPane.showMessageDialog(this, "Eliminada."); }
            else JOptionPane.showMessageDialog(this, "No se eliminó (¿no es tuya?).");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar:\n"+ex.getMessage());
        }
    }

    private void onDetail() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona una fila"); return; }
        Pelicula p = model.getAt(row);
        new DetallePeliculaDialog(this, p).setVisible(true);
    }

    private void onLogout() {
        dispose(); // muestra login desde Main
    }


    private void updateUserLabel() {         // <-- null-safe
        String who = "(sin sesión)";
        if (session != null && session.getCurrentUser() != null) {
            var u = session.getCurrentUser();
            if (u.getEmail() != null && !u.getEmail().isBlank()) who = u.getEmail();
            else if (u.getEmail() != null && !u.getEmail().isBlank()) who = u.getEmail();
            else if (u.getId() != null && !u.getId().isBlank()) who = u.getId();
        }
        lblUser.setText("👤 " + who);
        lblUser.setToolTipText("Sesión activa: " + who);
    }

    private String resolveUserLabel() {
        var u = session.getCurrentUser();

        try {
            if (u.getEmail() != null && !u.getEmail().isBlank()) return u.getEmail();
        } catch (Throwable ignored) {}
        try {
            if (u.getEmail() != null && !u.getEmail().isBlank()) return u.getEmail();
        } catch (Throwable ignored) {}
        return u.getId();
    }
}
