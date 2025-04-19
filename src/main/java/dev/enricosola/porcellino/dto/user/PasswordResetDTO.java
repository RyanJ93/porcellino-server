package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.EntityDTO;
import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordResetDTO extends EntityDTO<User> {
    private String password;
    private String token;

    /**
     * Generate a new entity and then hydrate it.
     *
     * @return The hydrated entity.
     */
    @Override
    public User toEntity() {
        return this.hydrateEntity(new User());
    }
}
