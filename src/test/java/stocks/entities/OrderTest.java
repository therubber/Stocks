package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    Security securityDow;

    @BeforeEach
    void setUp() {
        securityDow = new Security("UniRAK");
        securityDow.update();
        order = new Order(5, LocalDate.now(), "BUY", securityDow);
    }

    @Test
    void getCount() {
        assertEquals(5, order.getCount());
    }

    @Test
    void getExecutionPrice() {
        assertEquals(new BigDecimal(Double.toString(137.93)).setScale(2, RoundingMode.HALF_UP), order.getExecutionPrice());
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