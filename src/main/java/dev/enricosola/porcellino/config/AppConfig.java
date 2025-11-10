package dev.enricosola.porcellino.config;

import dev.enricosola.porcellino.service.IPAPIIPGeolocationService;
import dev.enricosola.porcellino.service.IPGeolocationService;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Configuration;
import dev.enricosola.porcellino.service.AESCryptoService;
import dev.enricosola.porcellino.service.CryptoService;
import org.springframework.context.annotation.Bean;

@Configuration
public class AppConfig {
    @Bean
    public IPGeolocationService ipGeolocationService() {
        return new IPAPIIPGeolocationService();
    }

    @Bean
    public CryptoService cryptoService() {
        return new AESCryptoService();
    }

    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().build();
    }
}
