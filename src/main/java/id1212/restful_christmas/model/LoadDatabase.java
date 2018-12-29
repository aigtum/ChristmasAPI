package id1212.restful_christmas.model;


import id1212.restful_christmas.repo.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(FilmRepository repository) {
        String about1 = "Despite having recently presided over a very successful Halloween, Jack Skellington, " +
                "aka the Pumpkin King, is bored with his job and feels that life in Halloweenland lacks meaning. " +
                "Then he stumbles upon Christmastown and promptly decides to make the Yuletide his own.";
        String about2 = "Chuck Jones' animated version of the classic Dr. Seuss book How the Grinch Stole " +
                "Christmas originally aired on television in 1966 and has since become a holiday family favorite." +
                " Voiced by Boris Karloff (who also narrates), the Grinch lives on top of a hill overlooking Whoville" +
                " with his dog, Max. Each year at Christmas time, the Grinch's hatred grows stronger toward those" +
                " insufferably cheerful Whos down in Whoville.";



        return args -> {
            log.info("Preloading " + repository.save(new Film("The Nightmare Before Christmas", 1993, about1, "Chuck Jones")));
            log.info("Preloading " + repository.save(new Film("How Grinch Stole The Christmas", 1967, about2, "Henry Selick")));
        };
    }
}
