package dev.enricosola.porcellino.facades;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class App {
    private static String url;

    /**
     * Configure the application global facade.
     *
     * @param applicationContext Current application execution context.
     */
    public static void configure(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        App.url = environment.getProperty("app.url");
    }

    /**
     * Return the public URL of the application.
     */
    public static String getUrl() {
        return App.url;
    }
}
