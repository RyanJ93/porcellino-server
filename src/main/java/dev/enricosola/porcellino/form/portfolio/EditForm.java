package dev.enricosola.porcellino.form.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditForm {
    @Size(max = 50, message = "You must provide a valid name.")
    @NotBlank(message = "You must provide a valid name.")
    private String name;
}
