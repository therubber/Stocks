package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.dows.SecurityDow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    Portfolio portfolio;
    SecurityDow securityDow;
    Order order;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio("test", "testUser", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP));
        securityDow = new SecurityDow("UniRAK");
        order = new Order(1, LocalDate.now(), "BUY", securityDow);
        securityDow.update();
        portfolio.orderInput(order);
    }

    @Test
    void getName() {
        assertEquals("test", portfolio.getName());
    }

    @Test
    void getEquity() {
        assertEquals(new BigDecimal(4862.07).setScale(2, RoundingMode.HALF_UP), portfolio.getEquity());
    }

    @Test
    void getStartequity() {
        assertEquals(new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP), portfolio.getStartEquity());
    }

    @Test
    void addPosition() {
        portfolio.orderInput(order);
        assertEquals(new BigDecimal(275.86).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
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
        assertEquals(new BigDecimal(137.93).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
    }

    @Test
    void setOwnedSecurities() {
        portfolio.setOwnedSecurities();
        assertTrue(portfolio.contains("UniRAK"));
    }
}