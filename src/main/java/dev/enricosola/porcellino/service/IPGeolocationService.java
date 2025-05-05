package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.IPGeolocationLookupException;
import dev.enricosola.porcellino.dto.IPGeolocationInfoDTO;

public interface IPGeolocationService {
    /**
     * Retrieves geolocation information for a given IP address.
     *
     * @param IPAddress The IP address for which geolocation information is being requested.
     * @return An instance of IPGeolocationInfoDTO containing details.
     * @throws IPGeolocationLookupException If an error occurs during the IP geolocation lookup process.
     */
    IPGeolocationInfoDTO lookup(String IPAddress) throws IPGeolocationLookupException;
}
