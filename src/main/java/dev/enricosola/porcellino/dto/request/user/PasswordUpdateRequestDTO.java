package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.request.RequestDTO;
import dev.enricosola.porcellino.dto.user.PasswordUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateRequestDTO extends RequestDTO {
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters long.")
    @NotBlank(message = "You must provide a valid password.")
    private String oldPassword;

    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters long.")
    @NotBlank(message = "You must provide a valid password.")
    private String newPassword;

    @Override
    public PasswordUpdateDTO toServiceDTO() {
        return new PasswordUpdateDTO(this.oldPassword, this.newPassword);
    }
}
