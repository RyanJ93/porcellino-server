package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.user.UserUpdateDTO;
import org.hibernate.validator.constraints.Length;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequestDTO extends RequestDTO {
    @Nullable
    @Length(max = 255, message = "Surname cannot be greater than 255 characters.")
    private String surname;

    @Nullable
    @Length(max = 255, message = "Name cannot be greater than 255 characters.")
    private String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserUpdateDTO toServiceDTO() {
        return new UserUpdateDTO(this.surname, this.name);
    }
}

