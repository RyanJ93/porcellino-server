package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getEmail());
    }

    private int id;
    private String email;
}
