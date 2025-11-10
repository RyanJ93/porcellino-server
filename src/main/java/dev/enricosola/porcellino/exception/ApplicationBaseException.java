package dev.enricosola.porcellino.exception;

import lombok.experimental.StandardException;
import java.io.Serial;

@StandardException
public abstract class ApplicationBaseException extends BaseException {
    @Serial
    private static final long serialVersionUID = 4657408646068738748L;

    /**
     * @{inheritDocs}
     */
    @Override
    public String getIdentifier() {
        return ApplicationBaseException.DEFAULT_IDENTIFIER;
    }
}
