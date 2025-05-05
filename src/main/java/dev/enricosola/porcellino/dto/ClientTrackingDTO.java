package dev.enricosola.porcellino.dto;

import dev.enricosola.porcellino.entity.ClientTracking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ClientTrackingDTO extends EntityDTO<ClientTracking> {
    /**
     * Creates a ClientTrackingDTO based on the provided reference information and tracking data derived from client device details and IP geolocation details.
     *
     * @param refName the reference name associated with the client tracking event
     * @param refId the unique reference ID for the client tracking event
     * @param clientDeviceInfoDTO the client device information object containing details about the user's browser, operating system, and user agent.
     * @param ipGeolocationInfoDTO the IP geolocation information object containing details about the user's geographic location, IP address, country, city, etc.
     * @return a ClientTrackingDTO instance populated with data extracted from the input objects
     */
    public static ClientTrackingDTO makeFromTrackingObjects(
            String refName,
            int refId,
            ClientDeviceInfoDTO clientDeviceInfoDTO,
            IPGeolocationInfoDTO ipGeolocationInfoDTO
    ) {
        return ClientTrackingDTO.builder()
                .countryCode(ipGeolocationInfoDTO.getCountryCode())
                .regionName(ipGeolocationInfoDTO.getRegionName())
                .IPAddress(ipGeolocationInfoDTO.getIpAddress())
                .countryName(ipGeolocationInfoDTO.getCountry())
                .cityName(ipGeolocationInfoDTO.getCity())
                .zipCode(ipGeolocationInfoDTO.getZip())
                .browserVersion(clientDeviceInfoDTO.getBrowserVersion())
                .browserName(clientDeviceInfoDTO.getBrowserName())
                .userAgent(clientDeviceInfoDTO.getUserAgent())
                .OSVersion(clientDeviceInfoDTO.getOSVersion())
                .OSName(clientDeviceInfoDTO.getOSName())
                .refName(refName)
                .refId(refId)
                .build();
    }

    private final String refName;
    private final int refId;
    private final String userAgent;
    private final String browserName;
    private final String browserVersion;
    private final String OSName;
    private final String OSVersion;
    private final String IPAddress;
    private final String countryName;
    private final String countryCode;
    private final String regionName;
    private final String cityName;
    private final String zipCode;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientTracking toEntity() {
        return this.hydrateEntity(new ClientTracking());
    }
}
