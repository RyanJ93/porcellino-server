package dev.enricosola.porcellino.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import dev.enricosola.porcellino.enums.StringToTransactionTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry formatterRegistry){
        formatterRegistry.addConverter(new StringToTransactionTypeConverter());
    }
}
