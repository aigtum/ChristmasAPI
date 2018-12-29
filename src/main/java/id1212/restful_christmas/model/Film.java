package id1212.restful_christmas.model;

/*
Model class/Domain object

Stores in H2 in-memory
 */

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "FILM")
public class Film {

    private @Id @GeneratedValue Long id;

    private String title;
    private int year;
    @Size(max = 100000)
    private String about;
    private String director;
    private int likes;

    Film(String title, int year, String about, String director) {
        this.title = title;
        this.year = year;
        this.about = about;
        this.director = director;
        this.likes = 0;
    }

    public void increaseLikes() {
        this.likes += 1;
    }
    // Required by JPA
    public Film() {}
}
