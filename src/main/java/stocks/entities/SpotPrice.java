package stocks.entities;

import java.time.LocalDate;

public class SpotPrice {

    private double spotPrice;
    private LocalDate date;

    public SpotPrice(double spotPrice) {
        this.spotPrice = spotPrice;
        date = LocalDate.now();
    }

    public String getDate() {
        return date.toString();
    }

    public double getSpotPrice() {
        return spotPrice;
    }
}
