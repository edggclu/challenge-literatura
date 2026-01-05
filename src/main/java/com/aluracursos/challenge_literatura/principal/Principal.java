package com.aluracursos.challenge_literatura.principal;

import com.aluracursos.challenge_literatura.model.Autor;
import com.aluracursos.challenge_literatura.model.DatosLibro;
import com.aluracursos.challenge_literatura.model.Libro;
import com.aluracursos.challenge_literatura.model.RespuestaAPI;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.service.ConsumoAPI;
import com.aluracursos.challenge_literatura.repository.LibroRepository;
import com.aluracursos.challenge_literatura.service.ConvierteDatos;
import org.springframework.boot.SpringApplication;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    List<Autor> autores = new ArrayList<>();
    List<Libro> libros = new ArrayList<>();

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros mas descargados
                    7 - Buscar autor por nombre

                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
            } catch (InputMismatchException e) {
            }
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresEntreAnios();
                    break;
                case 5:
                    buscarLibroPorIdioma();
                    break;
                case 6:
                    top10librosMasDescargados();
                case 7:
                    buscarAutorPorNombre();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("---- Ingrese autor que quieres buscar ----");
        var busqueda = teclado.nextLine();
        var autorBuscado = autorRepository.obtenerAutorPorNombre(busqueda);
        if(!autorBuscado.isEmpty()){
            System.out.println("---- Coincidencias con el nombre " + busqueda + " ----");
            autorBuscado.forEach(System.out::println);
        } else{
            System.out.println("Autor no encontrado");
        }
        System.out.println("------------------------------------------");

    }

    private void top10librosMasDescargados() {
        libros = libroRepository.top10LibrosMasDescargados();
        System.out.println("---- Top 10 libros mas descargados ----");
        libros.forEach(System.out::println);
        System.out.println("---------------------------------------");
    }

    private void buscarLibroPorIdioma() {
        var menu = """
                es - español
                en - ingles
                fr - frances
                pt - portugues
                """;
        System.out.println(menu);
        var opcion = teclado.nextLine();
        libros = libroRepository.findLibroByIdiomaContainsIgnoreCase(opcion);
        System.out.println("---- "+ (long) libros.size() +" Libro(s) en el idioma "+opcion+" ----");
        libros.forEach(System.out::println);
        System.out.println("--------------------------------------");


    }

    private void listarAutoresEntreAnios() {
        System.out.println("---- Ingrese el año vivo de autor(es) ----");
        try {
            var anio = (long) teclado.nextInt();
            teclado.nextLine();
            autores = autorRepository.obtenerAutoresVivosEnFecha(anio);
            System.out.println("---- Autores vivos durante el año " + anio + " ----");
            autores.forEach(System.out::println);
            System.out.println("---------------------------------------------------");
        } catch (InputMismatchException e) {
            System.out.println("Fecha invalida");
        }
    }

    private void listarAutoresRegistrados() {
        autores = autorRepository.findAll();
        System.out.println("---- Autores Registrados ----");
        autores.forEach(System.out::println);
        System.out.println("----------------------------");
    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        System.out.println("---- Libros registrados ----");
        libros.forEach(System.out::println);
        System.out.println("----------------------------");
    }

    private DatosLibro buscarLibroWeb(String busqueda) {
        var json = consumoApi.obtenerDatos(URL_BASE + busqueda.replace(" ", "%20"));
        RespuestaAPI datos = conversor.obtenerDatos(json, RespuestaAPI.class);
        try {
            var libroEnAPI = datos.libros().get(0);
            return libroEnAPI;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("---- Escribe el titulo del libro que deseas consultar ----");
        var busqueda = teclado.nextLine();
        var resultadoEnBaseDeDatos = libroRepository.findByTituloContainsIgnoreCase(busqueda);
        if (resultadoEnBaseDeDatos.isPresent()) {
            var libroEncontrado = resultadoEnBaseDeDatos.get();
            System.out.println("Libro en la base: " + libroEncontrado.getAutores().get(0) + "\n");
            System.out.println(libroEncontrado);
        } else {
            var busquedaAPI = buscarLibroWeb(busqueda);
            var nuevoLibro = busquedaAPI == null ? null : new Libro(busquedaAPI);
            if (nuevoLibro != null) {
                List<Autor> autores = busquedaAPI.autores().stream()
                        .map(Autor::new)
                        .collect(Collectors.toList());
                nuevoLibro.setAutores(autores);

                libroRepository.save(nuevoLibro);
                System.out.println("Libro nuevo guardado en la base de datos:\n");
                System.out.println(nuevoLibro);
            } else {
                System.out.println("Libro no encontrado\n");
            }
        }
    }
}
