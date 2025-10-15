package org.example.infra;

import org.example.model.Pelicula;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PeliculaRepository {
    List<Pelicula> findAllByUser(String userId) throws IOException;
    Optional<Pelicula> findById(String id, String userId) throws IOException;
    void add(Pelicula pelicula) throws IOException;                 // append
    boolean deleteById(String id, String userId) throws IOException; // reescribe CSV; true si eliminó
}
