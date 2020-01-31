package stocks.funds;

import java.util.LinkedList;

public class GenoAs {
    private static final String NAME = "GenoAs: 1";
    private static final String ISIN = "DE0009757682";
    private static final String WKN = "975768";
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
        return NAME + "   " + ISIN + "   " + WKN + "   " + getCurrentPrice() + " EUR" + System.lineSeparator();
    }
}
