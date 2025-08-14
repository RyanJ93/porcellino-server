package dev.enricosola.porcellino.dto.service.portfolio;

import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PortfolioUpdateDTO extends EntityDTO<Portfolio> {
    private final String name;

    @Override
    public Portfolio toEntity() {
        return this.hydrateEntity(new Portfolio());
    }
}
