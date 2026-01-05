package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("select a from Autor a where a.fechaNacimiento <= :anio and a.fechaMuerte>= :anio")
    List<Autor> obtenerAutoresVivosEnFecha(Long anio);

    //Optional<Autor> findAutorByNombreIsLikeIgnoreCase(String nombre);

    @Query("select a from Autor a where a.nombre ilike %:nombre%")
    List<Autor> obtenerAutorPorNombre(String nombre);

}
