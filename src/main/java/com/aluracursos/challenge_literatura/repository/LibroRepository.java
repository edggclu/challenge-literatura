package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Autor;
import com.aluracursos.challenge_literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

    List<Libro> findLibroByIdiomaContainsIgnoreCase(String idioma);
    @Query("select l from Libro l order by l.numeroDeDescargas desc limit 10")
    List<Libro> top10LibrosMasDescargados();

}
