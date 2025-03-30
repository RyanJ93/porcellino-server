package dev.enricosola.porcellino.support;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Component
public class SpringContext implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    @Override

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }
}
