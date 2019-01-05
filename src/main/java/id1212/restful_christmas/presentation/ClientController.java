package id1212.restful_christmas.presentation;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@Scope("session")
public class ClientController {

    @GetMapping("/")
    public String client(Model model) {
        model.addAttribute("getOneForm", new IdForm());
        model.addAttribute("deleteForm", new IdForm());
        model.addAttribute("filmForm", new FilmForm());
        model.addAttribute("searchForm", new SearchForm());
        return "client";
    }

    @GetMapping("/get_one")
    public String client(IdForm form) {
        Long id = form.getId();
        return "redirect:" + "/films/" + id;
    }

    @GetMapping("/search")
    public String search(SearchForm form) {
        String query = form.getQuery();

        return "redirect:" + "/films/?search=" + query;
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(IdForm form, Model model) {
        Long id = form.getId();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/films/" + id);

        return "Film deleted!";
    }



    @GetMapping("/update")
    public ResponseEntity<String> newFilm(FilmForm form, BindingResult bindingResult) {
        String newFilm;
        boolean createNew;


        createNew = form.getId() == null;

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body("Form error! Please check and try again");
        }


        if (!createNew) {
            newFilm = String.format("{\"title\": \"%s\", \"year\": %s, \"about\": \"%s\", \"director\": \"%s\"}",
                    form.getTitle(), form.getYear(), form.getAbout(), form.getDirector());
        } else {
            newFilm = String.format("{\"id\": \"%s\", \"title\": \"%s\", \"year\": %s, \"about\": \"%s\", \"director\": \"%s\"}",
                    form.getId(), form.getTitle(), form.getYear(), form.getAbout(), form.getDirector());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(newFilm, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response;

        if (createNew) {
            response = restTemplate.postForEntity("http://localhost:8080/films/", request , String.class );
        } else {
            try {
                restTemplate.put("http://localhost:8080/films/" + form.getId(), request);
                response = restTemplate.getForEntity("http://localhost:8080/films/" + form.getId(), String.class);
            } catch (HttpClientErrorException e) {
                System.err.println("Could not find movie with that id");
                response = ResponseEntity.ok("Could not find the ID, added with default instead");
            }
        }

        return response;
    }


}
