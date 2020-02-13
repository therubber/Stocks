package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.Securities;
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
        Securities.initiate();
        Securities.updatePrices();
        user = new User("user");
        order = new Order(5, LocalDate.now(), "BUY", Securities.get("UniRAK"));
        portfolio = new Portfolio("test", "user", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP));
        portfolio.orderInput(order);
        user.addPortfolio(portfolio);
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
    void getOrderHistory() {
        assertTrue(user.getOrderHistory().contains(order));
    }

    @Test
    void getPortfolios() {
        assertTrue(user.getPortfolios().contains(portfolio));
    }
}