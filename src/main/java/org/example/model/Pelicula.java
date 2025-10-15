package org.example.model;


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

    public Pelicula(String id, String title, int year, String director,
                    String description, String genero, String imageUrl, String userId) {
        this.id = id; this.title = title; this.year = year; this.director = director;
        this.description = description; this.genero = genero; this.imageUrl = imageUrl; this.userId = userId;
    }

    public static Pelicula fromCsv(String line) {
        String[] p = line.split(",", 8); // id,title,year,director,description,imageUrl,userId
        return new Pelicula(
                p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6], p[7]
        );
    }

    public String toCsv() {
        // Evitar romper el CSV si la descripción trae comas: las sustituimos (simple).
        String safeDesc = description == null ? "" : description.replace(",", " ");
        return String.join(",", id, title, String.valueOf(year), director, safeDesc, genero, imageUrl, userId);
    }

    // getters/setters
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

    @Override public boolean equals(Object o){ return o instanceof Pelicula v && Objects.equals(id,v.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
