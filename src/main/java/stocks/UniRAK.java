package stocks;

import java.util.LinkedList;

public class UniRAK {
    private static final String NAME = "UniRAK";
    private static final String ISIN = "DE0008491044";
    private static final int WKN = 849104;
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
