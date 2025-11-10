package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.clienttracking.NotFoundClientTrackingException;
import dev.enricosola.porcellino.repository.ClientTrackingRepository;
import dev.enricosola.porcellino.dto.IPGeolocationInfoDTO;
import dev.enricosola.porcellino.dto.ClientDeviceInfoDTO;
import dev.enricosola.porcellino.dto.ClientTrackingDTO;
import dev.enricosola.porcellino.entity.ClientTracking;
import dev.enricosola.porcellino.dto.ClientInfoDTO;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientTrackingService {
    protected final ClientTrackingRepository clientTrackingRepository;
    protected final IPGeolocationService ipGeolocationService;
    protected final ClientAnalyzeService clientAnalyzeService;

    /**
     * Finds a ClientTracking entity based on the provided reference name and reference ID.
     *
     * @param refName The reference name used to locate the ClientTracking entity.
     * @param refId The reference ID used to locate the ClientTracking entity.
     * @return The matching ClientTracking entity if found.
     * @throws NotFoundClientTrackingException if no ClientTracking entity is found for the provided reference name and ID.
     */
    public ClientTracking findByRef(String refName, int refId) {
        return this.clientTrackingRepository.findByRefNameAndRefId(refName, refId)
                .orElseThrow(() -> new NotFoundClientTrackingException("No client tracking found for refName \"" + refName + "\" and refId " + refId + "."));
    }

    /**
     * Finds a ClientTracking entity based on the provided reference name and reference ID and deletes the associated entity.
     *
     * @param refName The reference name used to locate the ClientTracking entity.
     * @param refId The reference ID used to locate the ClientTracking entity.
     * @throws NotFoundClientTrackingException if no ClientTracking entity is found for the provided reference name and ID.
     */
    public void findByRefAndDelete(String refName, int refId) {
        ClientTracking clientTracking = this.findByRef(refName, refId);
        this.clientTrackingRepository.delete(clientTracking);
    }

    /**
     * Tracks client-related information, including geolocation and device data,
     * and stores the tracking data based on the provided reference name and ID.
     *
     * @param refName The reference name associated with the tracking record.
     * @param refId The reference identifier associated with the tracking record.
     * @param clientInfoDTO The client information containing the IP address and user agent.
     * @return A {@link ClientTracking} instance representing the saved tracking record.
     */
    public ClientTracking track(String refName, int refId, ClientInfoDTO clientInfoDTO) {
        IPGeolocationInfoDTO ipGeolocationInfoDTO = this.ipGeolocationService.lookup(clientInfoDTO.getIPAddress());
        ClientDeviceInfoDTO clientDeviceInfoDTO = this.clientAnalyzeService.analyze(clientInfoDTO.getUserAgent());
        return this.create(ClientTrackingDTO.makeFromTrackingObjects(refName, refId, clientDeviceInfoDTO, ipGeolocationInfoDTO));
    }

    /**
     * Creates and saves a new ClientTracking entity to the repository.
     *
     * @param clientTrackingDTO The DTO containing the information to create a new ClientTracking entity.
     * @return A {@link ClientTracking} instance representing the saved entity.
     */
    public ClientTracking create(ClientTrackingDTO clientTrackingDTO) {
        return this.clientTrackingRepository.save(clientTrackingDTO.toEntity());
    }

    /**
     * Reprocesses all tracked data for a client based on the provided reference name and ID.
     * This includes reprocessing both geolocation information and client-specific information.
     *
     * @param refName The reference name associated with the client tracking record to be reprocessed.
     * @param refId The reference ID associated with the client tracking record to be reprocessed.
     */
    @Transactional
    public void reprocessAll(String refName, int refId) {
        this.reprocessClientInfo(refName, refId);
        this.reprocessGeoInfo(refName, refId);
    }

    /**
     * Reprocesses the geolocation information for a client tracking record based on the
     * provided reference name and reference ID. Updates the associated geolocation
     * details like country, region, city, and zip code using the IP address information.
     *
     * @param refName The reference name used to locate the client tracking record.
     * @param refId The reference ID used to locate the client tracking record.
     */
    public void reprocessGeoInfo(String refName, int refId) {
        ClientTracking clientTracking = this.findByRef(refName, refId);
        IPGeolocationInfoDTO ipGeolocationInfoDTO = this.ipGeolocationService.lookup(clientTracking.getIPAddress());
        clientTracking.setCountryCode(ipGeolocationInfoDTO.getCountryCode());
        clientTracking.setCountryName(ipGeolocationInfoDTO.getCountry());
        clientTracking.setRegionName(ipGeolocationInfoDTO.getRegion());
        clientTracking.setCityName(ipGeolocationInfoDTO.getCity());
        clientTracking.setZipCode(ipGeolocationInfoDTO.getZip());
        this.clientTrackingRepository.save(clientTracking);
    }

    /**
     * Reprocesses the client-specific information for a client tracking record
     * based on the provided reference name and ID. This includes updating details
     * such as browser version, browser name, operating system version, and
     * operating system name using the client’s user agent data.
     *
     * @param refName The reference name used to locate the client tracking record.
     * @param refId The reference ID used to locate the client tracking record.
     */
    public void reprocessClientInfo(String refName, int refId) {
        ClientTracking clientTracking = this.findByRef(refName, refId);
        ClientDeviceInfoDTO clientDeviceInfoDTO = this.clientAnalyzeService.analyze(clientTracking.getUserAgent());
        clientTracking.setBrowserVersion(clientDeviceInfoDTO.getBrowserVersion());
        clientTracking.setBrowserName(clientDeviceInfoDTO.getBrowserName());
        clientTracking.setOSVersion(clientDeviceInfoDTO.getOSVersion());
        clientTracking.setOSName(clientDeviceInfoDTO.getOSName());
        this.clientTrackingRepository.save(clientTracking);
    }
}
