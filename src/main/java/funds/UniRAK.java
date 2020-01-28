package funds;

import java.util.LinkedList;

public class UniRAK implements Fund {

    private final String name = "UniRAK";
    private final String isin = "DE0008491044";
    private final int wkn = 849104;
    private double currentPrice;
    private LinkedList<Double> histPrice;

    public String getName() {
        return name;
    }

    public String getISIN() {
        return isin;
    }

    public int getWKN() {
        return wkn;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public LinkedList<Double> histPrice() {
        return histPrice;
    }

    public void setHistPrice(LinkedList<Double> histPrice) {
        this.histPrice = histPrice;
    }

}
