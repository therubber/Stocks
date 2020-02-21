package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    Order order;
    Portfolio portfolio;
    UserRepo users;
    private final Factory factory = new Factory();

    @BeforeEach
    void setUp() {
        users.load();
        SecurityRepo securityRepo = new SecurityRepo();
        securityRepo.load();
        user = new User("user", "password");
        order = factory.createOrder(5, LocalDate.now(), "BUY", securityRepo.get("UniRAK"));
        portfolio = new Portfolio("test", "user", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP), new Input());
        user.addPortfolio(portfolio);
        portfolio.orderInput(order, users);
    }
    @Test
    void getUsername() {
        assertEquals("user", user.getUsername());
    }

    @Test
    void getEquity() {
        assertEquals(factory.bigDecimalFromInteger(10000), user.getEquity());
    }

    @Test
    void setEquity() {
        BigDecimal newEquity = factory.bigDecimalFromInteger(5000).setScale(2, RoundingMode.HALF_UP);
        user.setEquity(newEquity);
        assertEquals(newEquity, user.getEquity());
    }

    @Test
    void getPortfolios() {
        assertTrue(user.hasPortfolio(portfolio.getName()));
    }
}