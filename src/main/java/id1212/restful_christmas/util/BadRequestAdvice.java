package id1212.restful_christmas.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// 400 error handlers

@ControllerAdvice
public class BadRequestAdvice {

    // render to response body
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String wrongParametersHandler(MissingServletRequestParameterException e) {
        // returns the message of the exception
        return "Error 400 (Bad Request): The server could not understand the request due to invalid syntax.";
    }

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String Handler(NumberFormatException e) {
        // returns the message of the exception
        return "Error 400 (Bad Request): The server could not understand the request due to invalid syntax.";
    }
}
