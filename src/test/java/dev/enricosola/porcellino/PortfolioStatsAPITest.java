package dev.enricosola.porcellino;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import dev.enricosola.porcellino.support.TestAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import dev.enricosola.porcellino.service.TransactionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.RequestBuilder;
import dev.enricosola.porcellino.service.PortfolioService;
import org.springframework.test.web.servlet.ResultActions;
import dev.enricosola.porcellino.service.CurrencyService;
import dev.enricosola.porcellino.support.DatabaseCleaner;
import dev.enricosola.porcellino.enums.TransactionType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import dev.enricosola.porcellino.entity.Portfolio;
import dev.enricosola.porcellino.entity.Currency;
import dev.enricosola.porcellino.util.DateUtils;
import dev.enricosola.porcellino.entity.User;
import org.junit.jupiter.api.*;
import java.util.*;

record TransactionProps(double amount, int quantity, TransactionType type, String date){}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PortfolioStatsAPITest {
    static private final TransactionProps[] SAMPLE_TRANSACTION_LIST = {
        new TransactionProps(100, 1, TransactionType.OUT, "12-04-2024"),
        new TransactionProps(50, 1, TransactionType.IN, "12-04-2024"),
        new TransactionProps(25, 2, TransactionType.IN, "13-04-2024"),
        new TransactionProps(100, 1, TransactionType.IN, "01-04-2024"),
        new TransactionProps(25, 2, TransactionType.OUT, "16-04-2024"),
        new TransactionProps(100, 1, TransactionType.IN, "18-04-2024"),
        new TransactionProps(100, 1, TransactionType.OUT, "23-04-2024"),
        new TransactionProps(150, 2, TransactionType.OUT, "24-04-2024"),
        new TransactionProps(200, 1, TransactionType.IN, "04-03-2024"),
        new TransactionProps(50, 2, TransactionType.IN, "31-03-2024")
    };

    @Autowired
    private TestAuthenticationManager testAuthenticationManager;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MockMvc mockMvc;

    private boolean initialized = false;
    private Portfolio portfolio;

    private void generateSamplePortfolio(){
        User user = this.testAuthenticationManager.getAuthenticatedUser();
        Currency currency = this.currencyService.findById(1);
        //this.portfolio = this.portfolioService.create(user, currency, "Test stats");
    }

    private void generateSampleTransactions(){
        // ( -100 * 1 ) + ( 50 * 1 ) + ( 25 * 2 ) + ( 100 * 1 ) + ( -25 * 2 ) + ( 100 * 1 ) + ( -100 * 1 ) + ( -150 * 2 ) + ( 200 * 1 ) + ( 50 * 2 )
        for ( TransactionProps transactionProps : PortfolioStatsAPITest.SAMPLE_TRANSACTION_LIST ){
            Date date = DateUtils.parse(transactionProps.date());
            this.transactionService.create(this.portfolio, transactionProps.amount(), transactionProps.quantity(), transactionProps.type(), date, "");
        }
    }

    @BeforeEach
    public void setup(){
        if ( !this.initialized ){
            this.databaseCleaner.clean();
            this.testAuthenticationManager.ensureTestUser();
            this.generateSamplePortfolio();
            this.generateSampleTransactions();
            this.initialized = true;
        }
    }

    @Test
    @Order(1)
    @DisplayName("Testing portfolio stats computation for a given period.")
    public void portfolioStats() throws Exception {
        String url = "/api/portfolio/"  + this.portfolio.getId() + "/stats?endDate=25-04-2024";
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        int count = PortfolioStatsAPITest.SAMPLE_TRANSACTION_LIST.length;
        RequestBuilder requestBuilder = get(url).header("Authorization", "Bearer " + authenticationToken);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.stats.transactionCount").value(count))
                .andExpect(jsonPath("$.stats.balance").value(50));
    }

    @Test
    @Order(2)
    @DisplayName("Testing portfolio composition stats computation for a given period.")
    public void portfolioCompositionStats() throws Exception {
        String url = "/api/portfolio/"  + this.portfolio.getId() + "/composition-stats?endDate=25-04-2024";
        String authenticationToken = this.testAuthenticationManager.getAuthenticationToken();
        int count = PortfolioStatsAPITest.SAMPLE_TRANSACTION_LIST.length;
        RequestBuilder requestBuilder = get(url).header("Authorization", "Bearer " + authenticationToken);
        ResultActions resultActions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("SUCCESS"));
        resultActions.andExpect(jsonPath("$.compositionStats").exists());
        resultActions.andExpect(jsonPath("$.compositionStats.dailyCumulatedValues").exists());
        resultActions.andExpect(jsonPath("$.compositionStats.cumulatedValues.OUT").value(-550));
        resultActions.andExpect(jsonPath("$.compositionStats.cumulatedValues.IN").value(300));
        resultActions.andExpect(jsonPath("$.compositionStats.transactionCount").value(8));
        resultActions.andExpect(jsonPath("$.compositionStats.initialBalance").value(300));
        resultActions.andExpect(jsonPath("$.compositionStats.finalBalance").value(50));
        resultActions.andExpect(jsonPath("$.stats.balance").value(50));
        resultActions.andExpect(jsonPath("$.stats.transactionCount").value(count));
    }
}
