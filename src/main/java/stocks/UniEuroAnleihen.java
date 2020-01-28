package stocks;

import java.util.LinkedList;

public class UniEuroAnleihen {
    private static final String NAME = "UniEuroAnleihen";
    private static final String ISIN = "LU0966118209";
    private static final String WKN = "A1W4QB";
    private double currentPrice;
    private LinkedList<Double> histPrice;

    public String getName() {
        return NAME;
    }

    public String getISIN() {
        return ISIN;
    }

    public String getWKN() {
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
