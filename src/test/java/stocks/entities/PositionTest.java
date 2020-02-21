package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.SecurityRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private Position position;
    private  SecurityRepo securityRepo;
    private Factory factory = new Factory();


    @BeforeEach
    void setUp() {
        securityRepo.load();
        position = new Position(5, securityRepo.get("UniRAK"));
    }

    @Test
    void getCount() {
        assertEquals(5, position.getCount());
    }

    @Test
    void setCount() {
        position.setCount(6);
        assertEquals(6, position.getCount());
    }

    @Test
    void getSecurity() {
        assertEquals(securityRepo.get("UniRAK"), position.getSecurity());
    }

    @Test
    void getValue() {
        BigDecimal value = securityRepo.get("UniRAK").getSpotPrice().getPrice().multiply(factory.bigDecimalFromInteger(5));
        assertEquals(value, position.getValue());
    }

    @Test
    void getSpotPrice() {
        SpotPrice spotPrice = factory.createSpotPrice(138.33, LocalDate.parse("2020-02-19"));
        assertEquals(spotPrice, position.getSpotPrice());
    }

    @Test
    void getSecurityName() {
        assertEquals("UniRAK", position.getSecurityName());
    }

    @Test
    void getExecutionDate() {
        assertEquals(LocalDate.now(), position.getExecutionDate());
    }

    @Test
    void changeCount() {
        position.changeCount(4);
        assertEquals(9, position.getCount());
        position.changeCount(-2);
        assertEquals(7, position.getCount());
    }

    @Test
    void isZero() {
        position.setCount(0);
        assertTrue(position.isZero());
    }
}