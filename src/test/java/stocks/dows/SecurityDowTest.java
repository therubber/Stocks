package stocks.dows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.Securities;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SecurityDowTest {

    SecurityDow securityDow;

    @BeforeEach
    void setUp() {
        securityDow = new SecurityDow("UniRAK");
        securityDow.update();
    }

    @Test
    void getName() {
        assertEquals("UniRAK", securityDow.getName());
    }

    @Test
    void getSpotPrice() {
        assertEquals(new SpotPrice(137.93, LocalDate.parse("2020-02-13")), securityDow.getSpotPrice());
    }

    @Test
    void setSpotPrice() {
        securityDow.setSpotPrice(new SpotPrice(130.20, LocalDate.parse("2020-02-17")));
        assertEquals(new SpotPrice(130.20, LocalDate.parse("2020-02-17")), securityDow.getSpotPrice());
    }

    @Test
    void getSpotDate() {
        assertEquals(LocalDate.parse("2020-02-13"), securityDow.getSpotDate());
    }
}