package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    Security securityDow;
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();

    @BeforeEach
    void setUp() {
        securityDow = securityFactory.createSecurity("UniRAK");
        securityDow.update();
        order = portfolioFactory.createOrder(5, "BUY", securityDow);
    }

    @Test
    void getCount() {
        assertEquals(5, order.getCount());
    }

    @Test
    void getExecutionPrice() {
        assertEquals(numberFactory.createBigDecimal(138.33), order.getExecutionPrice());
    }

    @Test
    void getExecutionDate() {
        assertEquals(LocalDate.now(), order.getExecutionDate());
    }

    @Test
    void getType() {
        assertEquals("BUY", order.getType());
    }

    @Test
    void getSecurity() {
        assertEquals(securityDow, order.getSecurity());
    }
}