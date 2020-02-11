package stocks.dows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class SpotPrice {

    private BigDecimal price;
    private LocalDate date;

    /**
     * Constructor for regular use
     * @param spotPrice Spot price
     * @param date Date
     */
    public SpotPrice(double spotPrice, LocalDate date) {
        this.price = BigDecimal.valueOf(spotPrice).setScale(2, RoundingMode.CEILING);
        this.date = date;
    }

    /**
     * Getter method for date of price
     * @return String date value
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Getter method for price
     * @return Price value
     */
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPrice spotPrice = (SpotPrice) o;
        return Objects.equals(date, spotPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, date);
    }

    @Override
    public String toString() {
        return price + " / " + date;
    }
}
