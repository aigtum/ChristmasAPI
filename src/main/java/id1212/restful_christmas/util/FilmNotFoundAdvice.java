package id1212.restful_christmas.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// 404 error handlers


@ControllerAdvice
class FilmNotFoundAdvice {

    // render to response body
    @ResponseBody

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String filmNotFoundHandler(FilmNotFoundException e) {
        // returns the message of the exception
        return e.getMessage();
    }
}
