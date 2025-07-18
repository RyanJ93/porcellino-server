package dev.enricosola.porcellino.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TwoFactorAuthRecoveryCodeCollectionDTO {
    private final String[] recoveryCodeList;
}
