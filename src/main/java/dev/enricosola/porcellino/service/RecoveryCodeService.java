package dev.enricosola.porcellino.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import dev.enricosola.porcellino.repository.RecoveryCodeRepository;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.porcellino.exception.NotFoundException;
import dev.enricosola.porcellino.entity.RecoveryCode;
import dev.enricosola.porcellino.util.StringUtils;
import org.springframework.stereotype.Service;
import dev.enricosola.porcellino.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecoveryCodeService {
    private static final int CODE_LENGTH = 32;

    private final RecoveryCodeRepository recoveryCodeRepository;
    private final UserLookupService userLookupService;
    private final PasswordEncoder passwordEncoder;

    public RecoveryCodeService(
            RecoveryCodeRepository recoveryCodeRepository,
            UserLookupService userLookupService,
            PasswordEncoder passwordEncoder
    ) {
        this.recoveryCodeRepository = recoveryCodeRepository;
        this.userLookupService = userLookupService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all recovery codes for a given user that have not been invalidated.
     *
     * @param userId the ID of the user for whom recovery codes are to be fetched.
     * @return a list of active recovery codes associated with the specified user.
     */
    public List<RecoveryCode> findAll(int userId) {
        return this.recoveryCodeRepository.findAllByUserId(userId);
    }

    /**
     * Finds a recovery code for a specific user by matching the provided code.
     *
     * @param userId the ID of the user whose recovery codes are being searched.
     * @param code the plain text recovery code to match.
     * @return The matching recovery code if found.
     * @throws NotFoundException if no matching recovery code is found.
     */
    public RecoveryCode findByCode(int userId, String code) {
        List<RecoveryCode> recoveryCodeList = this.findAll(userId);
        return recoveryCodeList.stream()
                .filter(rc -> this.passwordEncoder.matches(code, rc.getCode()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No recovery code found matching given code found."));
    }

    /**
     * Finds a recovery code for a specific user by matching the provided code and marks it as invalid.
     *
     * @param userId the ID of the user whose recovery code is being processed.
     * @param code the plain text recovery code to match.
     * @return the recovery code that was found and invalidated.
     * @throws NotFoundException if no matching recovery code is found.
     */
    public RecoveryCode findByCodeAndInvalidate(int userId, String code) {
        RecoveryCode recoveryCode = this.findByCode(userId, code);
        recoveryCode.setInvalidatedAt(new Date());
        this.recoveryCodeRepository.save(recoveryCode);
        return recoveryCode;
    }

    /**
     * Generates a new recovery code for a specified user.
     *
     * @param userId the ID of the user for whom the recovery code is to be generated.
     * @return a newly created recovery code associated with the specified user.
     */
    public RecoveryCode generate(int userId) {
        User user = this.userLookupService.find(userId);
        return this.generateNoCheck(user);
    }

    /**
     * Generates multiple recovery codes for a specified user.
     *
     * @param userId the ID of the user for whom the recovery codes are to be generated.
     * @param amount the number of recovery codes to generate.
     * @return a list of newly created recovery codes associated with the specified user.
     */
    @Transactional
    public List<RecoveryCode> generateMulti(int userId, int amount) {
        List<RecoveryCode> recoveryCodeList = new ArrayList<>();
        User user = this.userLookupService.find(userId);
        for ( int i = 0 ; i < amount ; i++ ){
            RecoveryCode recoveryCode = this.generateNoCheck(user);
            recoveryCodeList.add(recoveryCode);
        }
        return recoveryCodeList;
    }

    /**
     * Invalidates all recovery codes associated with a specific user.
     *
     * @param userId the ID of the user whose recovery codes are to be invalidated.
     */
    public void invalidateAll(int userId) {
        this.recoveryCodeRepository.invalidateAll(userId);
    }

    /**
     * Generates a recovery code for the specified user without performing any additional checks.
     *
     * @param user the user for whom the recovery code is to be generated.
     * @return the newly created recovery code associated with the specified user.
     */
    protected RecoveryCode generateNoCheck(User user) {
        String plainTextCode = StringUtils.generateCryptoRandomString(RecoveryCodeService.CODE_LENGTH);
        String code = this.passwordEncoder.encode(plainTextCode);
        RecoveryCode recoveryCode = new RecoveryCode();
        recoveryCode.setPlainTextCode(plainTextCode);
        recoveryCode.setCode(code);
        recoveryCode.setUser(user);
        return this.recoveryCodeRepository.save(recoveryCode);
    }
}
