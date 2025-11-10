package dev.enricosola.porcellino.exception.user;

import dev.enricosola.porcellino.exception.annotation.Identifier;
import dev.enricosola.porcellino.exception.annotation.MuteReport;
import dev.enricosola.porcellino.exception.NotFoundException;
import lombok.experimental.StandardException;
import java.io.Serial;

@Identifier("exception.user.notFound")
@StandardException
@MuteReport
public class NotFoundUserException extends BaseUserException implements NotFoundException {
    @Serial
    private static final long serialVersionUID = -3375389883099464617L;
}
