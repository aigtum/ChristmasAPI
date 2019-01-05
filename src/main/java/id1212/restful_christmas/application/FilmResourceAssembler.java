package id1212.restful_christmas.application;

import id1212.restful_christmas.domain.Film;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class FilmResourceAssembler implements ResourceAssembler<Film, Resource<Film>> {

    @Override
    public Resource<Film> toResource(Film film) {
        // unconditional links

        Resource<Film> filmResource = new Resource<>(film,
                linkTo(methodOn(FilmController.class).one(film.getId())).withSelfRel(),
                linkTo(methodOn(FilmController.class).all()).withRel("films"));

        try {
            filmResource.add(linkTo(methodOn(FilmController.class).likeFilm(film.getId())).withRel("like"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return filmResource;
    }

}
