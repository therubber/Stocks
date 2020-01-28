package stocks;

import java.util.LinkedList;

public class UniAsia {
    private static final String NAME = "UniAsia";
    private static final String ISIN = "LU0037079034";
    private static final int WKN = 971267;
    private double currentPrice;
    private LinkedList<Double> histPrice;

    public String getName() {
        return NAME;
    }

    public String getISIN() {
        return ISIN;
    }

    public int getWKN() {
        return WKN;
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

    @Override
    public String toString() {
        return NAME + "   " + ISIN + "   " + WKN + "   " + getCurrentPrice() + " EUR";
    }
}
