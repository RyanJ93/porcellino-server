package dev.enricosola.porcellino.dto.auth;

import dev.enricosola.porcellino.dto.ClientInfoDTO;
import dev.enricosola.porcellino.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TwoFactorRecoveryCodeChallengeDTO implements DTO {
    private String recoveryCode;
    protected ClientInfoDTO clientInfoDTO;
}
