package org.example.infra;

import org.example.model.Pelicula;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CsvPeliculaRepository implements PeliculaRepository {
    private final Path file;          // p.ej.: Paths.get("src/main/resources/data/peliculas (1).csv")
    private final boolean hasHeader;  // marca si tu CSV tiene cabecera

    public CsvPeliculaRepository(Path file, boolean hasHeader) {
        this.file = file;
        this.hasHeader = hasHeader;
    }

    @Override
    public List<Pelicula> findAllByUser(String userId) throws IOException {
        ensureFile();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            List<Pelicula> out = new ArrayList<>();
            boolean skipHeader = hasHeader;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                if (line.isBlank()) continue;
                String[] p = line.split(",", 7);
                if (p.length < 7) continue;
                if (Objects.equals(p[6], userId)) {
                    out.add(new Pelicula(p[0], p[1], safeInt(p[2]), p[3], p[4], p[5], p[6]));
                }
            }
            return out;
        }
    }

    @Override
    public Optional<Pelicula> findById(String id, String userId) throws IOException {
        return findAllByUser(userId).stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @Override
    public void add(Pelicula pelicula) throws IOException {
        ensureFile();
        // Sanitiza descripción para no romper CSV si trae comas
        String safeDesc = pelicula.getDescription() == null ? "" : pelicula.getDescription().replace(",", " ");
        String line = String.join(",",
                pelicula.getId(),
                nullToEmpty(pelicula.getTitle()),
                String.valueOf(pelicula.getYear()),
                nullToEmpty(pelicula.getDirector()),
                safeDesc,
                nullToEmpty(pelicula.getImageUrl()),
                nullToEmpty(pelicula.getUserId())
        );
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            if (Files.size(file) > 0) bw.newLine();
            bw.write(line);
        }
    }

    @Override
    public boolean deleteById(String id, String userId) throws IOException {
        ensureFile();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<String> out = new ArrayList<>(lines.size());
        boolean removed = false;
        int start = 0;

        if (hasHeader && !lines.isEmpty()) {
            out.add(lines.get(0));
            start = 1;
        }

        for (int i = start; i < lines.size(); i++) {
            String ln = lines.get(i);
            if (ln.isBlank()) continue;
            String[] p = ln.split(",", 7);
            if (p.length < 7) continue;
            boolean match = p[0].equals(id) && p[6].equals(userId);
            if (match) { removed = true; continue; }
            out.add(ln);
        }

        if (removed) {
            Files.write(file, out, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        }
        return removed;
    }

    private void ensureFile() throws IOException {
        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
            if (hasHeader) {
                String header = "id,title,year,director,description,imageUrl,userId";
                Files.writeString(file, header, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    private static int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
    private static String nullToEmpty(String s){ return s == null ? "" : s; }
}
