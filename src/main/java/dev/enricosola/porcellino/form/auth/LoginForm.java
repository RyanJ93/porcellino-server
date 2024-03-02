package dev.enricosola.porcellino.form.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm implements CredentialsAwareForm {
    @Size(min = 6, max = 64)
    @NotBlank(message = "You must provide a valid password.")
    private String password;

    @NotBlank
    @Email(message = "You must provide your e-mail address.")
    private String email;
}
