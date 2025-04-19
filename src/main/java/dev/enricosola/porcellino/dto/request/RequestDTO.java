package dev.enricosola.porcellino.dto.request;

import dev.enricosola.porcellino.dto.DTO;

public abstract class RequestDTO {
    /**
     * Convert this DTO instance to another one that can be used in services.
     *
     * @return The generated DTO for service interaction.
     */
    public abstract DTO toServiceDTO();
}
