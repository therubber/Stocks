package stocks.daos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.factories.SecurityFactory;
import stocks.entities.Security;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    Security security;
    private final SecurityFactory securityFactory = new SecurityFactory();

    @BeforeEach
    void setUp() {
        security = securityFactory.createSecurity("UniRAK");
        security.update();
    }

    @Test
    void getName() {
        assertEquals("UniRAK", security.getName());
    }

    @Test
    void getSpotPrice() {
        assertEquals(securityFactory.createSpotPrice(138.33, LocalDate.parse("2020-02-19")), security.getSpotPrice());
    }

    @Test
    void setSpotPrice() {
        security.setSpotPrice(securityFactory.createSpotPrice(130.20, LocalDate.parse("2020-02-17")));
        assertEquals(securityFactory.createSpotPrice(130.20, LocalDate.parse("2020-02-17")), security.getSpotPrice());
    }

    @Test
    void getSpotDate() {
        assertEquals(LocalDate.parse("2020-02-19"), security.getSpotDate());
    }
}