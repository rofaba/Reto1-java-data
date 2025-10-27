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

    private static String[] splitFlexible(String line) {
        String sep = (line.chars().filter(ch -> ch == ';').count()
                >= line.chars().filter(ch -> ch == ',').count()) ? ";" : ",";
        return line.split(java.util.regex.Pattern.quote(sep), 8); // ← hasta 8 campos
    }

    @Override
    public List<Pelicula> findAllByUser(String userId) throws IOException {
        ensureFile();
        List<Pelicula> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line; boolean skip = hasHeader;
            while ((line = br.readLine()) != null) {
                if (skip) { skip = false; continue; }
                if (line.isBlank()) continue;
                if (line.charAt(0) == '\uFEFF') line = line.substring(1);

                String[] p = splitFlexible(line);
                if (p.length < 7) continue; // mínimo 7

                // Mapeo: 7 campos (sin género) o 8 campos (con género)
                String id       = p[0].trim();
                String title    = p[1].trim();
                String yearStr  = p[2].trim();
                String director = p[3].trim();
                String desc     = p[4].trim();
                String genero    = (p.length == 8 ? p[5].trim() : "");
                String imageUrl = (p.length == 8 ? p[6].trim() : p[5].trim());
                String uid      = (p.length == 8 ? p[7].trim() : p[6].trim());

                if (uid.equals(userId)) {
                    int year = 0; try { year = Integer.parseInt(yearStr); } catch (Exception ignore) {}

                    out.add(new Pelicula(id, title, year, director, desc, genero, imageUrl, uid));
                }
            }
        }
        return out;
    }



    @Override
    public Optional<Pelicula> findById(String id, String userId) throws IOException {
        return findAllByUser(userId).stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @Override
    public void add(Pelicula pelicula) throws IOException {
        ensureFile();
        String safeDesc = pelicula.getDescription() == null ? "" : pelicula.getDescription().replace(",", " ");
        String genre = pelicula.getGenero() == null ? "" : pelicula.getGenero().replace(",", " ");
        String line = String.join(",",
                pelicula.getId(),
                nullToEmpty(pelicula.getTitle()),
                String.valueOf(pelicula.getYear()),
                nullToEmpty(pelicula.getDirector()),
                safeDesc,
                genre,
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
            String[] p = ln.split(",", 8);
            if (p.length < 7) continue;
            boolean match = p[0].equals(id) && p[7].equals(userId);
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
                String header = "id,title,year,director,description,genre, imageUrl,userId";
                Files.writeString(file, header, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    private static int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
    private static String nullToEmpty(String s){ return s == null ? "" : s; }
}
