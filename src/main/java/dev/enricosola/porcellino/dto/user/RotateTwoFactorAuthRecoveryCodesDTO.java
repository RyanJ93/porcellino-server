package dev.enricosola.porcellino.dto.user;

import dev.enricosola.porcellino.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RotateTwoFactorAuthRecoveryCodesDTO implements DTO {
    private final String code;
}
