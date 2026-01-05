package com.aluracursos.challenge_literatura.model;

import jakarta.persistence.*;
import org.hibernate.engine.internal.Cascade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String titulo;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    List<Autor> autores = new ArrayList<>();;
    String idioma;
    Double numeroDeDescargas;

    public Libro(DatosLibro d){
        this.titulo = d.titulo();
        this.idioma = d.idiomas().isEmpty() ? "N/A" : d.idiomas().get(0);
        this.numeroDeDescargas = d.numeroDeDescargas();
    }

    public Libro() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
        autores.forEach(a -> a.getLibros().add(this));
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        String autoresStr = autores.stream()
                .map(a -> "Autor: " + a.getNombre())
                .collect(Collectors.joining("\n"));
        return "----- LIBRO -----\n" +
                "Titulo: " + titulo + "\n" +
                autoresStr +
                "\nIdioma: " + idioma + "\n" +
                "Numero de descargas: " + numeroDeDescargas.toString() + "\n" +
                "-----------------";
    }
}
