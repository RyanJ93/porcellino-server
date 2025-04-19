package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.EntityDTO;
import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateDTO extends EntityDTO<User> {
    private String email;

    /**
     * {@inheritDoc}
     */
    public User toEntity() {
        return this.hydrateEntity(new User());
    }
}
