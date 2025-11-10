package dev.enricosola.porcellino.service;

import dev.enricosola.porcellino.dto.ClientDeviceInfoDTO;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;
import nl.basjes.parse.useragent.UserAgent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAnalyzeService {
    private final UserAgentAnalyzer userAgentAnalyzer;

    /**
     * Analyzes the provided user agent to extract details about the user's device, including the operating system, browser, and their respective versions.
     *
     * @param userAgentString the user agent string to analyze.
     * @return a ClientDeviceInfoDTO containing the extracted device and browser information.
     */
    public ClientDeviceInfoDTO analyze(String userAgentString) {
        UserAgent userAgent = this.userAgentAnalyzer.parse(userAgentString);
        return ClientDeviceInfoDTO.builder()
                .OSVersion(userAgent.getValue(UserAgent.OPERATING_SYSTEM_VERSION))
                .OSName(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME))
                .browserVersion(userAgent.getValue(UserAgent.AGENT_VERSION))
                .browserName(userAgent.getValue(UserAgent.AGENT_NAME))
                .userAgent(userAgentString)
                .build();
    }
}
