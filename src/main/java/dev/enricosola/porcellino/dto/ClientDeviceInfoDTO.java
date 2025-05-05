package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ClientDeviceInfoDTO implements DTO {
    private final String userAgent;
    private final String browserName;
    private final String browserVersion;
    private final String OSName;
    private final String OSVersion;
}
