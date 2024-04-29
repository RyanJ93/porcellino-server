package dev.enricosola.porcellino;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import dev.enricosola.porcellino.support.TestAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.RequestBuilder;
import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.support.DatabaseCleaner;
import dev.enricosola.porcellino.service.CurrencyService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.entity.Currency;
import dev.enricosola.porcellino.entity.User;
import org.junit.jupiter.api.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TransactionAPITest {
    @Autowired
    private TestAuthenticationManager testAuthenticationManager;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private Portfolio portfolio;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        if ( this.portfolio == null ){
            this.databaseCleaner.clean();
            this.testAuthenticationManager.ensureTestUser();
            User user = this.testAuthenticationManager.getAuthenticatedUser();
            Currency currency = this.currencyService.getById(1);
            this.portfolio = this.portfolioService.create(user, currency, "TEST");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Testing invalid form data detection when creating a transaction.")
    public void invalidCreationParameters() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction/create";
        RequestBuilder requestBuilder = post(url)
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERR_INVALID_FORM"))
                .andExpect(jsonPath("$.errors.quantity[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.amount[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.date[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.type[0]").isNotEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("Testing new transaction creation.")
    public void transactionCreation() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction/create";
        RequestBuilder requestBuilder = post(url)
                .header("Authorization", "Bearer " + authenticationToken)
                .param("date", "2024-03-24T14:01:27.000Z")
                .param("quantity", "1")
                .param("amount", "10.1")
                .param("note", "TEST")
                .param("type", "OUT");
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transaction.id").isNotEmpty())
                .andExpect(jsonPath("$.transaction.date").value("2024-03-24T14:01:27.000+00:00"))
                .andExpect(jsonPath("$.transaction.amount").value("10.1"))
                .andExpect(jsonPath("$.transaction.quantity").value("1"))
                .andExpect(jsonPath("$.transaction.note").value("TEST"))
                .andExpect(jsonPath("$.transaction.type").value("OUT"));
    }

    @Test
    @Order(3)
    @DisplayName("Testing transaction listing.")
    public void transactionListing() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction";
        RequestBuilder requestBuilder = get(url)
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transactionList[0].id").isNotEmpty())
                .andExpect(jsonPath("$.transactionList[0].date").value("2024-03-24T14:01:27.000+00:00"))
                .andExpect(jsonPath("$.transactionList[0].amount").value("10.1"))
                .andExpect(jsonPath("$.transactionList[0].quantity").value("1"))
                .andExpect(jsonPath("$.transactionList[0].note").value("TEST"))
                .andExpect(jsonPath("$.transactionList[0].type").value("OUT"));
    }

    @Test
    @Order(4)
    @DisplayName("Testing transaction editing using some invalid data.")
    public void invalidTransactionEdit() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction/1/edit";
        RequestBuilder requestBuilder = patch(url)
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERR_INVALID_FORM"))
                .andExpect(jsonPath("$.errors.quantity[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.amount[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.date[0]").isNotEmpty())
                .andExpect(jsonPath("$.errors.type[0]").isNotEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Testing previously created transaction edit.")
    public void transactionEdit() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction/1/edit";
        RequestBuilder requestBuilder = patch(url)
                .header("Authorization", "Bearer " + authenticationToken)
                .param("date", "2024-03-25T14:01:27.000Z")
                .param("amount", "20.2")
                .param("note", "TEST 2")
                .param("quantity", "2")
                .param("type", "IN");
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transaction.id").value("1"))
                .andExpect(jsonPath("$.transaction.date").value("2024-03-25T14:01:27.000+00:00"))
                .andExpect(jsonPath("$.transaction.note").value("TEST 2"))
                .andExpect(jsonPath("$.transaction.amount").value("20.2"))
                .andExpect(jsonPath("$.transaction.quantity").value("2"))
                .andExpect(jsonPath("$.transaction.type").value("IN"));
    }

    @Test
    @Order(6)
    @DisplayName("Testing transaction delete.")
    public void transactionDelete() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction/1/delete";
        RequestBuilder requestBuilder = delete(url)
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @Order(7)
    @DisplayName("Testing transaction listing when there is no transaction left.")
    public void transactionEmptyListing() throws Exception {
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        String url = "/api/portfolio/" + this.portfolio.getId() + "/transaction";
        RequestBuilder requestBuilder = get(url)
                .header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.transactionList").isEmpty());
    }
}
