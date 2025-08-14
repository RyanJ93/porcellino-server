package dev.enricosola.porcellino.dto.service.portfolio;

import dev.enricosola.porcellino.support.annotation.MappingIgnore;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.dto.EntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PortfolioCreateDTO extends EntityDTO<Portfolio> {
    @MappingIgnore
    private final int currencyId;
    private final String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public Portfolio toEntity() {
        return this.hydrateEntity(new Portfolio());
    }
}
