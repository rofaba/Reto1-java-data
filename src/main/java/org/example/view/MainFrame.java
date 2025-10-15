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

        btnAdd = new JButton("Añadir");
        btnDelete = new JButton("Eliminar");
        btnDetail = new JButton("Detalle");
        btnLogout = new JButton("Cerrar sesión");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(btnAdd); top.add(btnDelete); top.add(btnDetail);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(btnLogout);
        JPanel north = new JPanel(new BorderLayout());
        north.add(top, BorderLayout.WEST);
        north.add(right, BorderLayout.EAST);

        setLayout(new BorderLayout(8,8));
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

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
    }

    public void loadData() {
        try {
            String userId = session.getCurrentUser().getId();
            List<Pelicula> pelis = repo.findAllByUser(userId);
            model = new PeliculaTableModel(pelis);
            table.setModel(model);
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
        dispose(); // cierra ventana principal; muestra login desde tu Main
    }
}
