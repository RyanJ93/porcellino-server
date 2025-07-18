package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TwoFactorAuthSetupDTO {
    protected final String secret;
    protected final String label;
    protected final String url;
}
