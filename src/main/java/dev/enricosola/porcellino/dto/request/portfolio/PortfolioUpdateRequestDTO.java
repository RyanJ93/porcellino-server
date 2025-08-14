package dev.enricosola.porcellino.dto.request.portfolio;

import dev.enricosola.porcellino.dto.service.portfolio.PortfolioUpdateDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class PortfolioUpdateRequestDTO extends RequestDTO {
    @Length(max = 64, message = "Portfolio name cannot be greater than 64 characters.")
    @NotBlank(message = "You must provide a name for portfolio.")
    private String name;

    /**
     * @{inheritDocs}
     */
    @Override
    public PortfolioUpdateDTO toServiceDTO() {
        return new PortfolioUpdateDTO(this.name);
    }
}
