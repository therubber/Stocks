package stocks.entities;

import java.time.LocalDate;

public class SpotPrice {

    private double price;
    private LocalDate date;

    public SpotPrice(double spotPrice) {
        this.price = spotPrice;
        date = LocalDate.now();
    }

    public String getDate() {
        return date.toString();
    }

    public double getSpotPrice() {
        return price;
    }
}
