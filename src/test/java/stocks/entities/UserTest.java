package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.factories.UserFactory;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;
    Order order;
    PortfolioSnapshot portfolioSnapshot;
    UserRepo users;
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final UserFactory userFactory = new UserFactory();




    @BeforeEach
    void setUp() {
        users = new UserRepo();
        users.load();
        SecurityRepo securityRepo = new SecurityRepo();
        securityRepo.load();
        user = userFactory.createUser("user", "password");
        order = portfolioFactory.createOrder(5, "BUY", securityRepo.get("UniRAK"));
        portfolioSnapshot = portfolioFactory.createPortfolioSnapshot("test", "user", numberFactory.createBigDecimal(5000), new Input());
        user.addPortfolio(portfolioSnapshot);
        portfolioSnapshot.orderInput(order, users);
    }
    @Test
    void getUsername() {
        assertEquals("user", user.getUsername());
    }

    @Test
    void getEquity() {
        assertEquals(numberFactory.createBigDecimal(10000), user.getEquity());
    }

    @Test
    void setEquity() {
        BigDecimal newEquity = numberFactory.createBigDecimal(5000);
        user.setEquity(newEquity);
        assertEquals(newEquity, user.getEquity());
    }

    @Test
    void getPortfolios() {
        assertTrue(user.hasPortfolio(portfolioSnapshot.getName()));
    }
}