package dev.enricosola.porcellino.config;

import dev.enricosola.porcellino.support.annotation.MappingIgnore;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import java.lang.reflect.Field;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition((MappingContext<Object, Object> context) -> {
            if (context.getSource() != null) {
                for (Field field : context.getSource().getClass().getDeclaredFields()) {
                    if (field.getAnnotation(MappingIgnore.class) != null) {
                        return false;
                    }
                }
            }
            return true;
        }).setSkipNullEnabled(true);
        return modelMapper;
    }
}
