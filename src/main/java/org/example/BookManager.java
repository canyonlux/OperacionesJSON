package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class BookManager {
    public static final String JSON_FILE = "books.json"; // Nombre del archivo JSON

    public static void main(String[] args) {
        List<Book> library = loadLibraryFromJSON(); // Cargar la lista de libros desde el archivo JSON
        System.out.println("Ruta del archivo JSON: " + new File(JSON_FILE).getAbsolutePath());

        while (true) {
            System.out.println("==== Gestión de Libros ====");
            System.out.println("1. Agregar un libro");
            System.out.println("2. Buscar libros por título o autor");
            System.out.println("3. Ver la lista de todos los libros");
            System.out.println("4. Salir");
            System.out.print("Elije una opción: ");

            Scanner scanner = new Scanner(System.in);
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    addBook(library); // Agregar un libro a la lista
                    break;
                case 2:
                    searchBooks(library); // Buscar libros por título o autor
                    break;
                case 3:
                    displayLibrary(library); // Mostrar la lista de libros
                    break;
                case 4:
                    saveLibraryToJSON(library); // Guardar la lista de libros en el archivo JSON antes de salir
                    System.out.println("Saliendo del programa. ¡Hasta pronto!");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción válida.");
            }
        }
    }

    private static List<Book> loadLibraryFromJSON() { // Cargar la lista de libros desde el archivo JSON
        ObjectMapper mapper = new ObjectMapper(); // Crear un objeto ObjectMapper
        List<Book> library = new ArrayList<>(); // Crear una nueva lista de libros
        try {
            /* File file = new File(JSON_FILE);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                library = mapper.readValue(file, new TypeReference<List<Book>>() {});
                System.out.println("Libros cargados desde el archivo JSON.");
            }
             */
            Path file = Path.of(JSON_FILE);

            library = mapper.readValue(file.toFile(), new TypeReference<>() {});

            System.out.println("Libros cargados desde el archivo JSON PATH.");
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo JSON. Creando una nueva lista.");
        }
        return library;
    }

    private static void saveLibraryToJSON(List<Book> library) { // Guardar la lista de libros en el archivo JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(JSON_FILE), library); // Convertir la lista de libros a formato JSON y guardarla en el archivo JSON
            System.out.println("Libros guardados en el archivo JSON.");
        } catch (IOException e) {
            System.out.println("No se pudo guardar en el archivo JSON.");
        }
    }

    private static void addBook(List<Book> library) { // Agregar un libro a la lista
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingresa los datos del libro:");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Autor: ");
        String author = scanner.nextLine();
        System.out.print("Número de páginas: ");
        int pages = scanner.nextInt();
        System.out.print("Año de publicación: ");
        int publicationYear = scanner.nextInt();

        Book newBook = new Book(isbn, title, author, pages, publicationYear); // Crear un nuevo objeto Book
        if (library.stream().noneMatch(book -> book.equals(newBook))) {
            library.add(newBook);
            System.out.println("Libro agregado con éxito.");
        } else {
            System.out.println("El libro ya existe en la lista.");
        }
    }

    private static void searchBooks(List<Book> library) { // Buscar libros por título o autor
        Scanner scanner = new Scanner(System.in);
        System.out.print("Buscar por título o autor: ");
        String keyword = scanner.nextLine();

        List<Book> results = library.stream()
                .filter(book -> book.getTitle().contains(keyword) || book.getAuthor().contains(keyword))
                .toList();

        if (!results.isEmpty()) {
            System.out.println("Resultados de la búsqueda:");
            results.forEach(System.out::println);
        } else {
            System.out.println("No se encontraron libros que coincidan con la búsqueda.");
        }
    }

    private static void displayLibrary(List<Book> library) { // Mostrar la lista de libros
        if (library.isEmpty()) {
            System.out.println("La lista de libros está vacía.");
        } else {
            System.out.println("Lista de libros:");
            library.forEach(System.out::println);
        }
    }
}

class Book {
    private String isbn;
    private String title;
    private String author;
    private int pages;
    private int publicationYear;

    public Book() { }

    public Book(String isbn, String title, String author, int pages, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.publicationYear = publicationYear;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public String toString() {
        return "Libro [ISBN: " + isbn + ", Título: " + title + ", Autor: " + author +
                ", Páginas: " + pages + ", Año de publicación: " + publicationYear + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
