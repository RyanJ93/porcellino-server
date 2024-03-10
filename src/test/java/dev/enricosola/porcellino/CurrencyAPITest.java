package dev.enricosola.porcellino;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import dev.enricosola.porcellino.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CurrencyAPITest {
    private String authenticationToken = null;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void authenticate(){
        if ( this.authenticationToken == null ){
            this.authenticationToken = this.authenticationService.authenticate("test@test.it", "test_password").getToken();
        }
    }

    @Order(1)
    @Test
    @DisplayName("Testing currency listing.")
    public void currencyList() throws Exception {
        RequestBuilder requestBuilder = get("/api/currency")
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.currencyList[0].id").isNotEmpty());
    }
}
