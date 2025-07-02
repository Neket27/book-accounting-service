package neket.bookaccounting.bookaccountingservice.controller.advice;

import neket.bookaccounting.bookaccountingservice.controller.advice.annotation.CustomExceptionHandler;
import neket.bookaccounting.bookaccountingservice.exeption.CreateException;
import neket.bookaccounting.bookaccountingservice.exeption.DeleteException;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import neket.bookaccounting.bookaccountingservice.exeption.UpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(annotations = CustomExceptionHandler.class)
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(Exception e) {
        return new Response(e.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new Response(errors.toString(), Instant.now().toString());
    }


    @ExceptionHandler(CreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleCreateException(CreateException ex) {
        return new Response(ex.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handlerUpdateException(UpdateException ex) {
        return new Response(ex.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handlerNotFoundException(NotFoundException ex) {
        return new Response(ex.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler(DeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response handlerDeleteException(DeleteException ex) {
        return new Response(ex.getMessage(), Instant.now().toString());
    }

}
