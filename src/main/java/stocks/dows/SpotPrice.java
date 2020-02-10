package stocks.dows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class SpotPrice {

    private BigDecimal price;
    private String date;

    /**
     * Empty constructor for serialization
     */
    public SpotPrice() {}

    /**
     * Constructor for regular use
     * @param spotPrice Spot price
     * @param date Date
     */
    public SpotPrice(double spotPrice, String date) {
        this.price = new BigDecimal(spotPrice).setScale(2, RoundingMode.CEILING);
        this.date = date;
    }

    /**
     * Getter method for date of price
     * @return String date value
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter method for date of price
     * @param date String date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter method for price
     * @return Price value
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Setter method for price
     * @param price Price to set
     */
    public void setPrice(double price) {
        this.price = new BigDecimal(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPrice spotPrice = (SpotPrice) o;
        return Objects.equals(price, spotPrice.price) &&
                Objects.equals(date, spotPrice.date);
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
