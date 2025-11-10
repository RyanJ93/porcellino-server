package dev.enricosola.porcellino.config;

import dev.enricosola.porcellino.support.annotation.MappingIgnore;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.ModelMapper;
import java.lang.reflect.Field;

@Configuration
public class MapperConfig {
    /**
     * Configures and returns a custom ModelMapper bean.
     * The method sets specific configurations for the ModelMapper instance, including:
     * - Custom property condition to skip mapping fields with the @MappingIgnore annotation.
     * - STRICT matching strategy to enforce an exact correspondence between source and target fields.
     * - Enabling the skipping of null values during property mapping to prevent null overwrites.
     *
     * @return a configured ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        var configuration = modelMapper.getConfiguration();
        configuration.setPropertyCondition((MappingContext<Object, Object> context) -> {
            if (context.getSource() != null) {
                for (Field field : context.getSource().getClass().getDeclaredFields()) {
                    if (field.getAnnotation(MappingIgnore.class) != null) {
                        return false;
                    }
                }
            }
            return true;
        });
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
        configuration.setSkipNullEnabled(true);
        return modelMapper;
    }
}
