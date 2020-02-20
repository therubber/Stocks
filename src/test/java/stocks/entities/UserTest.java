package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.SecurityRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    Order order;
    Portfolio portfolio;

    @BeforeEach
    void setUp() {
        SecurityRepo securityRepo = new SecurityRepo();
        securityRepo.load();
        user = new User("user", "password");
        order = new Order(5, LocalDate.now(), "BUY", securityRepo.get("UniRAK"));
        portfolio = new Portfolio("test", "user", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP));
        user.addPortfolio(portfolio);
        portfolio.orderInput(order);
    }
    @Test
    void getUsername() {
        assertEquals("user", user.getUsername());
    }

    @Test
    void getEquity() {
        assertEquals(new BigDecimal(10000).setScale(2, RoundingMode.HALF_UP), user.getEquity());
    }

    @Test
    void setEquity() {
        BigDecimal newEquity = new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP);
        user.setEquity(newEquity);
        assertEquals(newEquity, user.getEquity());
    }

    @Test
    void getPortfolios() {
        assertTrue(user.hasPortfolio(portfolio.getName()));
    }
}