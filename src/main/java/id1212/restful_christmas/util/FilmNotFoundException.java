package id1212.restful_christmas.util;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(Long id) {
        super("Could not find film with id: " + id);
    }
}
