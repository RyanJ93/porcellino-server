package dev.enricosola.porcellino.support.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEnvelope {
    private String recipient;
    private String subject;
}
