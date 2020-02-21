package stocks.daos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.Factory;
import stocks.entities.Security;
import stocks.entities.SpotPrice;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    Security security;
    private final Factory factory = new Factory();

    @BeforeEach
    void setUp() {
        security = factory.createSecurity("UniRAK");
        security.update();
    }

    @Test
    void getName() {
        assertEquals("UniRAK", security.getName());
    }

    @Test
    void getSpotPrice() {
        assertEquals(factory.createSpotPrice(138.33, LocalDate.parse("2020-02-19")), security.getSpotPrice());
    }

    @Test
    void setSpotPrice() {
        security.setSpotPrice(factory.createSpotPrice(130.20, LocalDate.parse("2020-02-17")));
        assertEquals(factory.createSpotPrice(130.20, LocalDate.parse("2020-02-17")), security.getSpotPrice());
    }

    @Test
    void getSpotDate() {
        assertEquals(LocalDate.parse("2020-02-19"), security.getSpotDate());
    }
}