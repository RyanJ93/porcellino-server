package dev.enricosola.porcellino.dto.response;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serial;
import java.util.List;
import lombok.Getter;
import java.util.Map;

public class ValidationExceptionResponseDTO extends ResponseDTO {
    @Serial
    private static final long serialVersionUID = 8894142645843584500L;

    @Getter
    private final String identifier = "exception.invalidInput";

    @Getter
    private final String message = "Provided input is invalid.";

    @Getter
    private final Map<String, List<String>> errors;

    public ValidationExceptionResponseDTO(MethodArgumentNotValidException ex) {
        this.errors = new HashMap<>();
        for ( ObjectError error : ex.getAllErrors() ) {
            String field = ((FieldError)error).getField();
            if ( !this.errors.containsKey(field) ) {
                this.errors.put(field, new ArrayList<>());
            }
            this.errors.get(field).add(error.getDefaultMessage());
        }
    }
}
