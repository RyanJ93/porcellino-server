package dev.enricosola.porcellino.dto.response.user;

import dev.enricosola.porcellino.dto.response.ResponseDTO;
import dev.enricosola.porcellino.entity.User;
import java.io.Serializable;
import java.util.Date;
import java.io.Serial;
import lombok.Getter;

@Getter
public class UserResponseDTO extends ResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1382727181881515680L;
    
    private final String id;
    private final String surname;
    private final String name;
    private final String email;
    private final Date createdAt;
    private final Date updatedAt;

    public UserResponseDTO(User user) {
        this.id = String.valueOf(user.getId());
        this.surname = user.getSurname();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
