package dev.enricosola.porcellino.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serial;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class BindingErrorResponse extends ErrorResponse implements Response, Serializable {
    @Serial
    private static final long serialVersionUID = -3926126175341297670L;
    private static final String DEFAULT_STATUS = "ERR_INVALID_FORM";

    private Map<String, List<String>> errors;

    private void extractErrors(BindingResult bindingResult){
        List<ObjectError> errorList = bindingResult.getAllErrors();
        this.errors = new HashMap<>();
        for ( ObjectError error : errorList ){
            String field = ((FieldError)error).getField();
            if ( !this.errors.containsKey(field) ){
                this.errors.put(field, new ArrayList<>());
            }
            this.errors.get(field).add(error.getDefaultMessage());
        }
    }

    public BindingErrorResponse(BindingResult bindingResult){
        super(BindingErrorResponse.DEFAULT_STATUS);
        this.extractErrors(bindingResult);
    }
}
