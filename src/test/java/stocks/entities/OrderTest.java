package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.dows.SecurityDow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    SecurityDow securityDow;

    @BeforeEach
    void setUp() {
        securityDow = new SecurityDow("UniRAK");
        securityDow.update();
        order = new Order(5, LocalDate.now(), "BUY", securityDow);
    }

    @Test
    void getCount() {
        assertEquals(5, order.getCount());
    }

    @Test
    void getExecutionPrice() {
        assertEquals(new BigDecimal(137.93).setScale(2, RoundingMode.HALF_UP), order.getExecutionPrice());
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