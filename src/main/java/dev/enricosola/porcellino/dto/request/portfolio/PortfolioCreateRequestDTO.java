package dev.enricosola.porcellino.dto.request.portfolio;

import dev.enricosola.porcellino.dto.service.portfolio.PortfolioCreateDTO;
import dev.enricosola.porcellino.dto.request.RequestDTO;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class PortfolioCreateRequestDTO extends RequestDTO {
    @Min(value = 1, message = "You must select a currency for portfolio.")
    @NotNull(message = "You must define a currency for portfolio.")
    private int currencyId;

    @Length(max = 64, message = "Portfolio name cannot be greater than 64 characters.")
    @NotBlank(message = "You must provide a name for portfolio.")
    private String name;

    /**
     * @{inheritDocs}
     */
    @Override
    public PortfolioCreateDTO toServiceDTO() {
        return new PortfolioCreateDTO(this.currencyId, this.name);
    }
}
