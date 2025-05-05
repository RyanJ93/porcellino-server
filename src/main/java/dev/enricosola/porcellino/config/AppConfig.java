package dev.enricosola.porcellino.config;

import dev.enricosola.porcellino.service.IPAPIIPGeolocationService;
import dev.enricosola.porcellino.service.IPGeolocationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class AppConfig {
    @Bean
    public IPGeolocationService ipGeolocationService() {
        return new IPAPIIPGeolocationService();
    }
}
