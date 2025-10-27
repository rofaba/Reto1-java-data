package org.example.model;
import java.lang.reflect.Constructor;

import java.util.Objects;

public class Pelicula {
    private String id;
    private String title;
    private int year;
    private String director;
    private String description;
    private String genero;
    private String imageUrl;
    private String userId;

    /**
     * Constructor de la clase {@code Pelicula}.
     *
     * @param id          Identificador único de la película.
     * @param title       Título de la película.
     * @param year        Año de lanzamiento de la película.
     * @param director    Director de la película.
     * @param description Descripción breve de la película.
     * @param genero      Género cinematográfico de la película.
     * @param imageUrl    URL del cartel o imagen de la película.
     * @param userId      Identificador del usuario propietario de la película.
     */

    public Pelicula(String id, String title, int year, String director,
                    String description, String genero, String imageUrl, String userId) {
        this.id = id; this.title = title; this.year = year; this.director = director;
        this.description = description; this.genero = genero; this.imageUrl = imageUrl; this.userId = userId;
    }

    /**
     * Crea un objeto {@code Pelicula} a partir de una línea en formato CSV.
     *
     * @param line Línea en formato CSV.
     * @return Objeto {@code Pelicula}.
     */
    public static Pelicula fromCsv(String line) {
        String[] p = line.split(",", 8); // id,title,year,director,description,imageUrl,userId
        return new Pelicula(
                p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6], p[7]
        );
    }

     /**
     * Convierte el objeto {@code Pelicula} a una representación en formato CSV.
     *
     * @return Representación en formato CSV.
     */
    public String toCsv() {
        String safeDesc = description == null ? "" : description.replace(",", " ");
        return String.join(",", id, title, String.valueOf(year), director, safeDesc, genero, imageUrl, userId);
    }


    // getters & setters
    public String getId(){ return id; }
    public String getTitle(){ return title; }
    public int getYear(){ return year; }
    public String getDirector(){ return director; }
    public String getGenero() { return genero;  }
    public String getDescription(){ return description; }
    public String getImageUrl(){ return imageUrl; }
    public String getUserId(){ return userId; }
    public void setId(String id){ this.id = id; }
    public void setTitle(String title){ this.title = title; }
    public void setYear(int year){ this.year = year; }
    public void setDirector(String director){ this.director = director; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDescription(String description){ this.description = description; }
    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
    public void setUserId(String userId){ this.userId = userId; }

    @Override public boolean equals(Object o){
        return o instanceof Pelicula v && Objects.equals(id,v.id);
    }
    @Override public int hashCode(){
        return Objects.hash(id);
    }
}
