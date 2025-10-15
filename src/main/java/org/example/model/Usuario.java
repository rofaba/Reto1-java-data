package org.example.model;

import java.util.Objects;

public class Usuario {
    private String id;
    private String email;
    private String password;

    public Usuario(String id, String email, String password) {
        this.id = id; this.email = email; this.password = password;
    }

    public static Usuario fromCsv(String line) {
        String[] p = line.split(",", 3); // id,email,password
        return new Usuario(p[0], p[1], p[2]);
    }

    public String toCsv() { return String.join(",", id, email, password); }

    // getters/setters
    public String getId(){ return id; }
    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
    public void setId(String id){ this.id = id; }
    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }

    @Override public boolean equals(Object o){ return o instanceof Usuario u && Objects.equals(id,u.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
