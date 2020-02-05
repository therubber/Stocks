package stocks.dows;

import java.util.Objects;

public class SpotPrice {

    private double price;
    private String date;

    public SpotPrice(double spotPrice, String date) {
        this.price = spotPrice;
        this.date = date;
    }

    String getDate() {
        return date;
    }

    double getSpotPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPrice spotPrice = (SpotPrice) o;
        return Double.compare(spotPrice.price, price) == 0 &&
                Objects.equals(date, spotPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, date);
    }
}
