package dev.enricosola.porcellino.exception;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import dev.enricosola.porcellino.response.ExceptionErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import dev.enricosola.porcellino.response.BindingErrorResponse;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BindingErrorResponse(ex));
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    protected ResponseEntity<ExceptionErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionErrorResponse(ex, "ERR_NOT_FOUND"));
    }

    @ExceptionHandler({ BadCredentialsException.class })
    protected ResponseEntity<ExceptionErrorResponse> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionErrorResponse(ex, "ERR_UNAUTHORIZED"));
    }
}
