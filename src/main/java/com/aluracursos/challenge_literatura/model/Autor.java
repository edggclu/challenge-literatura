package com.aluracursos.challenge_literatura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;
    private int fechaNacimiento;
    private int fechaMuerte;
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();;

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }


    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.fechaNacimiento = d.fechaNacimiento();
        this.fechaMuerte = d.fechaMuerte();
    }

    public Autor() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(int fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        String librosStr = libros.stream()
                .map(a -> "Libro: " + a.getTitulo())
                .collect(Collectors.joining("\n"));
        return "----- AUTOR -----\n" +
                "Nombre: " + nombre + "\n" +
                librosStr+
                "\nAño de nacimiento: " + Integer.valueOf(fechaNacimiento).toString() + "\n" +
                "Año de muerte: " + Integer.valueOf(fechaMuerte).toString() + "\n" +
                "-----------------";
    }
}
