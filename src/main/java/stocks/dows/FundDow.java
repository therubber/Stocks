package stocks.dows;

import stocks.interfaces.Fund;

import java.util.LinkedList;
import java.util.Objects;

public class FundDow implements Fund {

    private String name;
    private String isin;
    private String wkn;
    private double spotPrice;
    private LinkedList<Double> histPrices;

    public FundDow (String name) {
        this.name = name;
    }

    public FundDow(String name, String isin, String wkn, double spotPrice) {
        this.name = name;
        this.isin = isin;
        this.wkn = wkn;
        this.spotPrice = spotPrice;
    }

    public String getName() {
        return name;
    }

    public String getIsin() {
        return isin;
    }

    public String getWkn() {
        return wkn;
    }

    public double getSpotPrice() {
        return spotPrice;
    }

    public void setCurrentPrice(double spotPrice) {
        this.spotPrice = spotPrice;
    }

    public LinkedList<Double> histPrices() {
        return histPrices;
    }

    public void setHistPrices(LinkedList<Double> histPrice) {
        this.histPrices = histPrice;
    }

    @Override
    public String toString() {
        return name + "   " + isin + "   " + wkn + "   " + getSpotPrice() + " EUR" + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FundDow fundDow = (FundDow) o;
        return Objects.equals(name, fundDow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
