package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class IPGeolocationInfoDTO {
    private final String ipAddress;
    private final String country;
    private final String countryCode;
    private final String region;
    private final String regionName;
    private final String city;
    private final String zip;
    private final double latitude;
    private final double longitude;
    private final String timezone;
    private final String isp;
    private final String org;
}
