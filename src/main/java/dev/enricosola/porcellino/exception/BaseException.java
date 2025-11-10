package dev.enricosola.porcellino.exception;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import dev.enricosola.porcellino.exception.annotation.MuteReport;
import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public abstract class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8766931528797198526L;
    public static final String DEFAULT_IDENTIFIER = "exception.general";
    
    /**
     * Retrieves the identifier associated with the exception instance.
     *
     * @return a string representing the unique identifier for the exception.
     */
    public String getIdentifier() {
        if ( this.getClass().isAnnotationPresent(Identifier.class) ) {
            return this.getClass().getAnnotation(Identifier.class).value();
        }
        return BaseException.DEFAULT_IDENTIFIER;
    }

    /**
     * Determines whether this exception should be reported.
     *
     * @return {@code true} if the exception should be reported; {@code false} otherwise.
     */
    public boolean shouldBeReported() {
        return !this.getClass().isAnnotationPresent(MuteReport.class);
    }
}
