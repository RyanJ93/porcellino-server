package dev.enricosola.porcellino.exception;

import java.util.Map;

public interface ContextAware {
    /**
     * Retrieves the context associated with the implementing object.
     *
     * @return a map containing context data as key-value pairs, where keys are of type String and values are of type Object.
     */
    Map<String, Object> getContext();
}
