package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.Securities;
import stocks.repo.Users;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    Portfolio portfolio;
    Security securityDow;
    Order order;

    @BeforeEach
    void setUp() {
        Securities.load();
        securityDow = Securities.get("UniRAK");
        Users.add(new User("testUser", "password"));
        portfolio = new Portfolio("test", "testUser", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP));
        order = new Order(1, LocalDate.now(), "BUY", securityDow);
        portfolio.orderInput(order);
    }

    @Test
    void getName() {
        assertEquals("test", portfolio.getName());
    }

    @Test
    void getEquity() {
        assertEquals(new BigDecimal(Double.toString(4861.67)).setScale(2, RoundingMode.HALF_UP), portfolio.getEquity());
    }

    @Test
    void getStartequity() {
        assertEquals(new BigDecimal(Double.toString(5000)).setScale(2, RoundingMode.HALF_UP), portfolio.getStartEquity());
    }

    @Test
    void addPosition() {
        portfolio.orderInput(order);
        assertEquals(new BigDecimal(Double.toString(276.66)).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void getPosition() {
        assertEquals(new Position(order), portfolio.getPosition(0));
    }

    @Test
    void getPositionCount() {
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void deletePosition() {
        portfolio.deletePosition(new Position(order));
        assertEquals(0, portfolio.getPositionCount());
    }

    @Test
    void getValue() {
        assertEquals(new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP), portfolio.getValue());
    }

    @Test
    void getPositionValue() {
        assertEquals(new BigDecimal(Double.toString(138.33)).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
    }

    @Test
    void loadOwnedSecurities() {
        portfolio.loadOwnedSecurities();
        assertTrue(portfolio.contains("UniRAK"));
    }

    @Test
    void orderInput() {
        Position position = portfolio.getPosition(0);
        assertEquals(1, position.getCount());
        portfolio.orderInput(new Order(1, LocalDate.now(), "SELL", securityDow));
        assertEquals(0, portfolio.getPositionCount());
    }
}