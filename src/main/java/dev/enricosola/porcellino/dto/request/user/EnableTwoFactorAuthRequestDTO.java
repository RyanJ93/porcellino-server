package dev.enricosola.porcellino.dto.request.user;

import dev.enricosola.porcellino.dto.user.EnableTwoFactorAuthDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = false)
public class EnableTwoFactorAuthRequestDTO extends RequestDTO  {
    @NotBlank(message = "You must provide a valid OTP code.")
    @Length(min = 6, max = 6, message = "The OTP code must be 6 digits long.")
    private String code;

    @Override
    public EnableTwoFactorAuthDTO toServiceDTO() {
        return new EnableTwoFactorAuthDTO(this.code);
    }
}
