package dev.enricosola.porcellino.form.user;

import dev.enricosola.porcellino.form.auth.CredentialsAwareForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupForm implements CredentialsAwareForm {
    @Size(min = 6, max = 64)
    @NotBlank(message = "You must provide a password.")
    private String password;

    @NotBlank
    @Email(message = "You must provide your e-mail address.")
    private String email;
}
