package org.example;

import org.example.controller.MainController;
import org.example.infra.*;
import org.example.view.MainFrame;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("[BOOT] arranque");

                Path usuariosCsv  = Paths.get("data","usuarios.csv");
                Path peliculasCsv = Paths.get("data","peliculas.csv");

                UsuarioRepository userRepo = new CsvUsuarioRepository(usuariosCsv, false);
                PeliculaRepository peliRepo = new CsvPeliculaRepository(peliculasCsv, false);

                SessionContext session = SessionContext.get();
                AuthProvider auth = new CsvAuthProvider(userRepo, session);

                System.out.println("[PATH] usuarios:  " + usuariosCsv.toAbsolutePath());
                System.out.println("[PATH] peliculas: " + peliculasCsv.toAbsolutePath());
                System.out.println("[PATH] usuarios existe? " + java.nio.file.Files.exists(usuariosCsv));
                System.out.println("[PATH] usuarios bytes:  " + java.nio.file.Files.size(usuariosCsv));
                System.out.println("[PATH] peliculas existe? " + java.nio.file.Files.exists(peliculasCsv));
                System.out.println("[PATH] peliculas bytes:  " + java.nio.file.Files.size(peliculasCsv));

                MainController controller = new MainController(peliRepo, auth, session);
                MainFrame frame = new MainFrame(peliRepo, session);

                System.out.println("[BOOT] init controller");
                controller.init(frame);  //
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error iniciando la aplicación:\n" + e.getMessage(),
                        "Error crítico", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
