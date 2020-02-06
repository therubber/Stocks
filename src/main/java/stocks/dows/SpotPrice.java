package stocks.dows;

import java.util.Objects;

public class SpotPrice {

    private double price;
    private String date;

    public SpotPrice() {}

    public SpotPrice(double spotPrice, String date) {
        this.price = spotPrice;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    @Override
    public String toString() {
        return price + "/" + date;
    }
}
