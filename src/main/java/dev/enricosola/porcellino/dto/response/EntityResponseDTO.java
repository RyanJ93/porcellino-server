package dev.enricosola.porcellino.dto.response;

import dev.enricosola.porcellino.support.SpringContext;
import org.springframework.context.ApplicationContext;
import org.modelmapper.ModelMapper;
import java.io.Serializable;

public abstract class EntityResponseDTO<E extends Serializable> extends ResponseDTO {
    protected static <E extends Serializable, D extends EntityResponseDTO<E>> D buildResponseDTOFromEntity(E entity, Class<D> responseDTOClass) {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        ModelMapper modelMapper = applicationContext.getBean(ModelMapper.class);
        return modelMapper.map(entity, responseDTOClass);
    }
}
