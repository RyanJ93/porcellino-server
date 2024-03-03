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
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@ControllerAdvice
@SuppressWarnings("NullableProblems")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BindingErrorResponse(ex));
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    protected ResponseEntity<ExceptionErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex){
        RestResponseEntityExceptionHandler.logger.error(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionErrorResponse(ex, "ERR_NOT_FOUND"));
    }

    @ExceptionHandler({ BadCredentialsException.class })
    protected ResponseEntity<ExceptionErrorResponse> handleBadCredentials(BadCredentialsException ex){
        RestResponseEntityExceptionHandler.logger.error(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionErrorResponse(ex, "ERR_UNAUTHORIZED"));
    }
    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    protected ResponseEntity<ExceptionErrorResponse> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex){
        String message = ex.getLocalizedMessage();
        RestResponseEntityExceptionHandler.logger.error(message);
        if ( message.contains("Duplicate entry") && message.contains("users.users_email") ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionErrorResponse(ex, "ERR_EMAIL_ADDRESS_TAKEN"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionErrorResponse(ex, "ERROR"));
    }
}
