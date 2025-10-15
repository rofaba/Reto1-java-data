package org.example.infra;

import org.example.model.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;

public class CsvUsuarioRepository implements UsuarioRepository {
    private final Path file;
    private final boolean hasHeader;

    public CsvUsuarioRepository(Path file, boolean hasHeader) {
        this.file = file;
        this.hasHeader = hasHeader;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) throws IOException {
        ensureFile();
        String target = email.trim().toLowerCase(); // email case-insensitive
        try (var br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            boolean skip = hasHeader;
            while ((line = br.readLine()) != null) {
                if (skip) { skip = false; continue; }
                if (line.isBlank()) continue;

                String[] p = line.split(",", 3); // id,email,password
                if (p.length < 3) continue;

                String id  = p[0].trim();
                String em  = p[1].trim();
                String pass= p[2].trim();

                if (em.toLowerCase().equals(target)) {
                    return Optional.of(new Usuario(id, em, pass));
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public Optional<Usuario> findById(String id) throws IOException {
        return readFirstMatch(0, id);
    }

    private Optional<Usuario> readFirstMatch(int idx, String value) throws IOException {
        ensureFile();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            boolean skipHeader = hasHeader;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                if (line.isBlank()) continue;
                String[] p = line.split(",", 3); // id,email,password
                if (p.length < 3) continue;
                if (p[idx].equals(value)) {
                    return Optional.of(new Usuario(p[0], p[1], p[2]));
                }
            }
        }
        return Optional.empty();
    }

    private void ensureFile() throws IOException {
        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
            if (hasHeader) {
                String header = "id,email,password";
                Files.writeString(file, header, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }
}
