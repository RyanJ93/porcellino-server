package dev.enricosola.porcellino.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.enricosola.porcellino.exception.BaseException;
import dev.enricosola.porcellino.exception.ContextAware;
import java.io.Serial;
import lombok.Getter;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponseDTO extends ResponseDTO {
    @Serial
    private static final long serialVersionUID = -1054944555733947039L;

    @Getter
    private final String identifier;

    @Getter
    private final String message;

    @Getter
    private final Map<String, Object> context;

    public ExceptionResponseDTO(Throwable ex) {
        this.identifier = ex instanceof BaseException ? ((BaseException) ex).getIdentifier() : "exception.error";
        this.context = ex instanceof ContextAware ? ((ContextAware) ex).getContext() : null;
        this.message = ex.getMessage();
    }
}
