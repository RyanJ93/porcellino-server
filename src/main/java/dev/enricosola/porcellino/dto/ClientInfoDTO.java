package dev.enricosola.porcellino.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ClientInfoDTO implements DTO {
    /**
     * Builds a ClientInfoDTO instance by extracting the user agent and IP address from the provided request.
     *
     * @param httpServletRequest the HttpServletRequest object containing client information.
     * @return a ClientInfoDTO instance containing the extracted user agent and IP address.
     */
    public static ClientInfoDTO buildFromHttpRequest(HttpServletRequest httpServletRequest) {
        return ClientInfoDTO.builder()
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .IPAddress(httpServletRequest.getRemoteAddr())
                .build();
    }

    private final String userAgent;
    private final String IPAddress;
}
