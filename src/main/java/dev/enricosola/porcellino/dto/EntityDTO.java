package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.support.SpringContext;
import org.springframework.context.ApplicationContext;
import org.modelmapper.ModelMapper;
import java.io.Serializable;

public abstract class EntityDTO<E extends Serializable> implements DTO {
    /**
     * Hydrate a given entity instance using this DTO instance's property values.
     *
     * @param entity The entity to hydrate.
     *
     * @return The hydrated entity.
     */
    public E hydrateEntity(E entity) {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        ModelMapper modelMapper = applicationContext.getBean(ModelMapper.class);
        modelMapper.map(this, entity);
        return entity;
    }

    /**
     * Generate a new entity and then hydrate it.
     *
     * @return The hydrated entity.
     */
    public abstract E toEntity();
}
