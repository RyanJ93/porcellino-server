package dev.enricosola.porcellino.support.email;

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

public class TemplateEngine {
    /**
     * Load the template file contents from the given path.
     *
     * @param templatePath Path to the template file.
     *
     * @return The template file contents.
     */
    private String loadTemplateFileContents(String templatePath){
        try {
            InputStream inputStream = new ClassPathResource(templatePath).getInputStream();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inject the given variables into the template contents.
     *
     * @param contents The template file contents.
     * @param variables The variables to inject.
     *
     * @return The processed template file contents.
     */
    private String injectVariables(String contents, Map<String, String> variables) {
        for ( Map.Entry<String, String> entry : variables.entrySet() ) {
            contents = contents.replace("{{ " + entry.getKey() + " }}", entry.getValue());
        }
        return contents;
    }

    /**
     * Render the template file contents with the given variables.
     *
     * @param templatePath Path to the template file.
     * @param variables The variables to inject.
     *
     * @return The loaded and processed template file contents.
     */
    public String render(String templatePath, Map<String, String> variables) {
        String contents = this.loadTemplateFileContents(templatePath);
        if ( variables != null && !variables.isEmpty() ) {
            contents = this.injectVariables(contents, variables);
        }
        return contents;
    }
}
