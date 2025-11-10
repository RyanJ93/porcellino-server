package dev.enricosola.porcellino.exception;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import dev.enricosola.porcellino.dto.response.ValidationExceptionResponseDTO;
import dev.enricosola.porcellino.exception.annotation.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import dev.enricosola.porcellino.dto.response.ExceptionResponseDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ValidationExceptionResponseDTO(ex));
    }

    @ExceptionHandler({ BaseException.class })
    protected ResponseEntity<ExceptionResponseDTO> handleException(BaseException ex) {
        int statusCode = 500;
        if ( ex.getClass().isAnnotationPresent(HttpStatusCode.class) ) {
            statusCode =  ex.getClass().getAnnotation(HttpStatusCode.class).value();
        }
        if ( ex.shouldBeReported() ) {
            log.error(ex.getMessage(), ex);
        }
        return ResponseEntity.status(statusCode).body(new ExceptionResponseDTO(ex));
    }

    @ExceptionHandler({ Throwable.class })
    protected ResponseEntity<ExceptionResponseDTO> handleException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponseDTO(ex));
    }
}
