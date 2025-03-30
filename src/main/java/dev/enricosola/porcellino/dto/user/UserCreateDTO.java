package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.EntityDTO;
import dev.enricosola.porcellino.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCreateDTO extends EntityDTO<User> {
    protected String email;
    private String password;

    public User toEntity() {
        return this.hydrateEntity(new User());
    }

    public UserAuthDTO toUserAuthDTO() {
        return new UserAuthDTO(this.email, this.password);
    }
}
