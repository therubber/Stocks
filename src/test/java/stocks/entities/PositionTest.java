package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.factories.UserFactory;
import stocks.repo.SecurityRepo;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private Position position;
    private  SecurityRepo securityRepo;
    private final NumberFactory numberFactory = new NumberFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();


    @BeforeEach
    void setUp() {
        securityRepo = new SecurityRepo();
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
        BigDecimal value = securityRepo.get("UniRAK").getSpotPrice().getPrice().multiply(numberFactory.createBigDecimal(5));
        assertEquals(value, position.getValue());
    }

    @Test
    void getSpotPrice() {
        SpotPrice spotPrice = securityFactory.createSpotPrice(138.33, LocalDate.parse("2020-02-19"));
        assertEquals(spotPrice, position.getSpotPrice());
    }

    @Test
    void getSecurityName() {
        assertEquals("UniRAK", position.getSecurity().getName());
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