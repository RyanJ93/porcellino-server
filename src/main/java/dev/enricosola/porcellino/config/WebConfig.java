package dev.enricosola.porcellino.config;

import dev.enricosola.porcellino.interceptor.PortfolioOwnershipCheckInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import dev.enricosola.porcellino.enums.StringToTransactionTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.service.UserService;
import org.springframework.format.FormatterRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new PortfolioOwnershipCheckInterceptor(this.portfolioService, this.userService));
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry){
        formatterRegistry.addConverter(new StringToTransactionTypeConverter());
    }
}
