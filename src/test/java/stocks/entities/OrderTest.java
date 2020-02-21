package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.utility.Factory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    Security securityDow;
    private final Factory factory = new Factory();

    @BeforeEach
    void setUp() {
        securityDow = factory.createSecurity("UniRAK");
        securityDow.update();
        order = factory.createOrder(5, LocalDate.now(), "BUY", securityDow);
    }

    @Test
    void getCount() {
        assertEquals(5, order.getCount());
    }

    @Test
    void getExecutionPrice() {
        assertEquals(factory.bigDecimalFromDouble(138.33), order.getExecutionPrice());
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