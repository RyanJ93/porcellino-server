package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.exception.IPGeolocationLookupException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import dev.enricosola.porcellino.dto.IPGeolocationInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.Arrays;
import java.net.URI;

public class IPAPIIPGeolocationService implements IPGeolocationService {
    private static final String[] LOCAL_IP_ADDRESSES = {"127.0.0.1", "0:0:0:0:0:0:0:1"};
    private static final String API_URL = "http://ip-api.com/json/";

    /**
     * Retrieves geolocation information for a given IP address.
     *
     * @param IPAddress The IP address for which geolocation information is being requested.
     * @return An instance of IPGeolocationInfoDTO containing details.
     * @throws IPGeolocationLookupException If an error occurs during the IP geolocation lookup process.
     */
    public IPGeolocationInfoDTO lookup(String IPAddress) throws IPGeolocationLookupException {
        if ( Arrays.asList(IPAPIIPGeolocationService.LOCAL_IP_ADDRESSES).contains(IPAddress) ){
            return IPGeolocationInfoDTO.builder().ipAddress(IPAddress).build();
        }
        IPAPIResponse ipapiResponse = this.fetchGeolocationData(IPAddress);
        return IPGeolocationInfoDTO.builder()
                .countryCode(ipapiResponse.countryCode)
                .regionName(ipapiResponse.regionName)
                .timezone(ipapiResponse.timezone)
                .country(ipapiResponse.country)
                .region(ipapiResponse.region)
                .longitude(ipapiResponse.lon)
                .latitude(ipapiResponse.lat)
                .city(ipapiResponse.city)
                .zip(ipapiResponse.zip)
                .isp(ipapiResponse.isp)
                .org(ipapiResponse.org)
                .ipAddress(IPAddress)
                .build();
    }

    /**
     * Sends an HTTP GET request to the API to retrieve geolocation data for a given IP address.
     *
     * @param IPAddress The IP address for which the request will be sent.
     * @return The HTTP response obtained.
     * @throws IOException If an I/O error occurs while sending or receiving during the request.
     * @throws InterruptedException If the operation is interrupted while waiting for the response.
     */
    @SuppressWarnings("resource")
    private HttpResponse<String> sendRequest(String IPAddress) throws IOException, InterruptedException {
        URI uri = URI.create(IPAPIIPGeolocationService.API_URL + IPAddress);
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Fetches geolocation data for the specified IP address.
     *
     * @param IPAddress The IP address for which geolocation data is being fetched.
     * @return An instance of IPAPIResponse containing geolocation details for the given IP address.
     * @throws IPGeolocationLookupException If an error occurs during the geolocation lookup process.
     */
    private IPAPIResponse fetchGeolocationData(String IPAddress) throws IPGeolocationLookupException {
        try {
            HttpResponse<String> httpResponse = this.sendRequest(IPAddress);
            if ( httpResponse.statusCode() == 429 ){
                throw new IPGeolocationLookupException("IP lookup rate limit exceeded.");
            }
            IPAPIResponse ipapiResponse = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(httpResponse.body(), IPAPIResponse.class);
            if ( !ipapiResponse.status.equals("success") ){
                throw new IPGeolocationLookupException("Unable to fetch geo location data for IP " + IPAddress + ".");
            }
            return ipapiResponse;
        } catch (IOException|InterruptedException ex) {
            throw new IPGeolocationLookupException("Unable to perform IP lookup.", ex);
        }
    }
}

class IPAPIResponse {
    public String status;
    public String country;
    public String countryCode;
    public String region;
    public String regionName;
    public String city;
    public String zip;
    public double lat;
    public double lon;
    public String timezone;
    public String isp;
    public String org;
}
