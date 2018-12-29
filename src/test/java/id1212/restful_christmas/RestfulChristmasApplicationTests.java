package id1212.restful_christmas;

import id1212.restful_christmas.model.Film;
import id1212.restful_christmas.repo.FilmRepository;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class RestfulChristmasApplicationTests {

    @Autowired
    private FilmRepository repository;

    private Film film1;
    private Film film2;
    private int numOfFilms;

    @Before
    public void init() {
        numOfFilms = (int) repository.count();

        film1 = new Film();
        film1.setTitle("Film1");
        film1.setYear(1998);
        film1.setAbout("about1");
        film1.setDirector("director1");
        repository.save(film1);

        film2 = new Film();
        film2.setTitle("Film2");
        film2.setYear(1998);
        film2.setAbout("about2");
        film2.setDirector("director2");
        repository.save(film2);
    }

    @Test
    public void shouldHaveFilmsInDatabase () {
        List<Film> result = repository.findAll();
        assertThat(result, not(empty()));
        assertThat(result.size(), equalTo(numOfFilms + 2));
    }

    @Test
    public void shouldHaveFilm1InDatabase() {
        List<Film> result = repository.findAll();
        assertThat(film1, isIn(result));
    }

    @Test
    public void shouldBeAbleToAddNewMovies() throws IOException {
        String expected = "{\"id\":5,\"title\":\"Elf\",\"year\":2003,\"about\":[\"Will Ferrell\",\"Zooey Deschanel\"],\"director\":\"Jon Favreau\",\"likes\":0,\"_links\":{\"self\":{\"href\":\"http://localhost:8080/films/5\"},\"films\":{\"href\":\"http://localhost:8080/films\"},\"like\":{\"href\":\"http://localhost:8080/films/5/like\"}}}";
        HttpPost post = new HttpPost("http://localhost:8080/films");
        String json = "{\"id\":\"5\", \"title\": \"Elf\", \"year\": \"2003\", \"starring\": [\"Will Ferrell\", \"Zooey Deschanel\"], \"director\": \"Jon Favreau\"}";
        StringEntity entity = new StringEntity(json);

        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        System.out.println(EntityUtils.toString(post.getEntity()));

        HttpResponse response = HttpClientBuilder.create().build().execute(post);

        String result = EntityUtils.toString(response.getEntity());
        assertThat(result, equalTo(expected));
    }


    @Test
    public void shouldResultIn200WhenCorrect() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/films");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldGetCorrectResponseWhenSearchingForSingleFilm() throws IOException {
        String film = "{\"id\":1,\"title\":\"The Nightmare Before Christmas\",\"year\":1993,\"starring\":[\"Boris Carloff\",\"June Foray\"],\"director\":\"Chuck Jones\",\"likes\":0,\"_links\":{\"self\":{\"href\":\"http://localhost:8080/films/1\"},\"films\":{\"href\":\"http://localhost:8080/films\"},\"like\":{\"href\":\"http://localhost:8080/films/1/like\"}}}";

        HttpUriRequest request = new HttpGet("http://localhost:8080/films/1");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        String result = EntityUtils.toString(httpResponse.getEntity());

        assertThat(result, equalTo(film));
    }

    @Test
    public void shouldBeAbleToDelete() throws IOException {
        HttpUriRequest delete = new HttpDelete("http://localhost:8080/films/3");
        HttpResponse deleteResponse = HttpClientBuilder.create().build().execute(delete);

        assertThat(deleteResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));

        HttpUriRequest request = new HttpGet("http://localhost:8080/films/3");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(httpResponse.getEntity()), equalTo("Could not find film with id: 3"));

    }





}

