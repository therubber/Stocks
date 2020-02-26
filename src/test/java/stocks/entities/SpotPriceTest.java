package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.SpotPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SpotPriceTest {

    SpotPrice spotPrice;

    @BeforeEach
    void setUp() {
        spotPrice = new SpotPrice(130.70, LocalDate.now());
    }

    @Test
    void getDate() {
        assertEquals(LocalDate.now(), spotPrice.getDate());
    }

    @Test
    void getPrice() {
        assertEquals(new BigDecimal(Double.toString(130.70)).setScale(2, RoundingMode.HALF_UP), spotPrice.getPrice());
    }
}