package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import dev.enricosola.porcellino.service.UserService;
import org.springframework.stereotype.Component;
import dev.enricosola.porcellino.entity.User;
import java.util.Optional;

@Component
public class TestAuthenticationManager {
    private static final String TEST_USER_PASSWORD = "test_password";
    private static final String TEST_USER_EMAIL = "test@test.it";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    private String authenticationToken = null;
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

    public String getAuthenticationToken(){
        if ( this.authenticationToken == null ){
            this.authenticationToken = this.authenticationService.authenticate(TEST_USER_EMAIL, TEST_USER_PASSWORD).getToken();
            this.authenticatedUser = this.userService.getUserByEmail(TEST_USER_EMAIL).orElseThrow(() -> {
                return new RuntimeException("Test user not found.");
            });
        }
        return this.authenticationToken;
    }

    private User getAuthenticatedUser(){
        return this.authenticatedUser;
    }
}
