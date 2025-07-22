package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.EntityDTO;
import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InitEmailChangeDTO extends EntityDTO<User> {
    private String email;

    @Override
    public User hydrateEntity(User user) {
        user.setPendingEmail(this.email);
        return user;
    }

    @Override
    public User toEntity() {
        return this.hydrateEntity(new User());
    }
}
