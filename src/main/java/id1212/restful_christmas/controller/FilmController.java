package id1212.restful_christmas.controller;

import id1212.restful_christmas.model.Film;
import id1212.restful_christmas.repo.FilmRepository;
import id1212.restful_christmas.util.FilmNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;



// the body returns at once, not waiting for view processing
@RestController
class FilmController {
    private final FilmRepository repository;
    private final FilmResourceAssembler assembler;

    // repository is injected into controller
    FilmController(FilmRepository repository, FilmResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    /* GET */
    /* ROOT */
    @GetMapping(path = "/films", produces = MediaType.APPLICATION_JSON_VALUE)
    Resources<Resource<Film>> all() {
        List<Resource<Film>> films = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(films,
                linkTo(methodOn(FilmController.class).all()).withSelfRel());
    }

    /* POST */
    @PostMapping(path = "/films")
    ResponseEntity<?> newFilm(@RequestBody Film newFilm) throws URISyntaxException {
        Resource<Film> resource = assembler.toResource(repository.save(newFilm));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }


    /* GET */
    @GetMapping(path = "/films/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Resource<Film> one(@PathVariable Long id) {

        Film film = repository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));

        return assembler.toResource(film);
    }

    /* PUT */
    @PutMapping("/films/{id}")
    ResponseEntity<?> replaceFilm(@RequestBody Film newFilm, @PathVariable Long id) throws URISyntaxException {

        Film updatedFilm = repository.findById(id)
                .map(film -> {
                    film.setTitle(newFilm.getTitle());
                    film.setYear(newFilm.getYear());
                    film.setAbout(newFilm.getAbout());
                    film.setDirector(newFilm.getDirector());
                    return repository.save(film);
                })
                .orElseGet(() -> {
                    newFilm.setId(id);
                    return repository.save(newFilm);
                });

        Resource<Film> resource = assembler.toResource(updatedFilm);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    /* GET */
    @GetMapping("/films/search?{query}&{param}")
    ResponseEntity<?> search(@RequestParam String query, @RequestParam String param) {
        System.out.println("Q: " + query + " P: " + param);
        return ResponseEntity.ok().build();
    }

    /* GET */
    @GetMapping("/films/{id}/like")
    ResponseEntity<?> likeFilm(@PathVariable Long id) throws URISyntaxException {
        Film film = repository.findById(id).orElseThrow(() -> new FilmNotFoundException(id));

        film.increaseLikes();
        repository.save(film);
        Resource<Film> resource = assembler.toResource(film);

        //return ResponseEntity.ok(assembler.toResource();
        return ResponseEntity.ok().build();
    }


    /* DELETE */
    @DeleteMapping("/films/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
