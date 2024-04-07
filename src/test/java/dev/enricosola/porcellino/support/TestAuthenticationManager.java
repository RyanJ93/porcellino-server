package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import dev.enricosola.porcellino.service.UserService;
import org.springframework.stereotype.Component;
import dev.enricosola.porcellino.entity.User;
import java.util.Optional;
import lombok.Getter;

@Component
public class TestAuthenticationManager {
    private static final String SECONDARY_TEST_USER_EMAIL = "test_secondary@test.it";
    private static final String TEST_USER_PASSWORD = "test_password";
    private static final String TEST_USER_EMAIL = "test@test.it";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    private String secondaryAuthenticationToken = null;
    private String authenticationToken = null;

    @Getter
    private User secondaryAuthenticatedUser = null;

    @Getter
    private User authenticatedUser = null;

    public String getTestUserPassword(){
        return TestAuthenticationManager.TEST_USER_PASSWORD;
    }

    public String getTestUserEmail(){
        return TestAuthenticationManager.TEST_USER_EMAIL;
    }

    public void ensureTestUser(){
        Optional<User> user = this.userService.getUserByEmail(TEST_USER_EMAIL);
        if ( user.isEmpty() ){
            this.userService.create(TEST_USER_EMAIL, TEST_USER_PASSWORD);
            this.getAuthenticationToken();
        }
    }

    public void ensureSecondaryTestUser(){
        Optional<User> user = this.userService.getUserByEmail(SECONDARY_TEST_USER_EMAIL);
        if ( user.isEmpty() ){
            this.userService.create(SECONDARY_TEST_USER_EMAIL, TEST_USER_PASSWORD);
            this.getSecondaryAuthenticationToken();
        }
    }

    public String getAuthenticationToken(){
        if ( this.authenticationToken == null ){
            this.authenticationToken = this.authenticationService.authenticate(TEST_USER_EMAIL, TEST_USER_PASSWORD).getToken();
            this.authenticatedUser = this.userService.getUserByEmail(TEST_USER_EMAIL).orElseThrow(() -> {
                return new RuntimeException("Test user not found.");
            });
        }
        return this.authenticationToken;
    }

    public String getSecondaryAuthenticationToken(){
        if ( this.secondaryAuthenticationToken == null ){
            this.secondaryAuthenticationToken = this.authenticationService.authenticate(SECONDARY_TEST_USER_EMAIL, TEST_USER_PASSWORD).getToken();
            this.secondaryAuthenticatedUser = this.userService.getUserByEmail(SECONDARY_TEST_USER_EMAIL).orElseThrow(() -> {
                return new RuntimeException("Test user not found.");
            });
        }
        return this.secondaryAuthenticationToken;
    }
}
