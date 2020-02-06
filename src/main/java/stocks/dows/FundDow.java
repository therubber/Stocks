package stocks.dows;

import stocks.interfaces.Fund;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FundDow implements Fund {

    private String name;
    private String isin;
    private String wkn;
    private SpotPrice spotPrice;
    private List<SpotPrice> historicalPrices = new LinkedList<>();

    public FundDow() {}

    public FundDow (String name) {
        this.name = name;
    }

    public FundDow(String name, String isin, String wkn) {
        this.name = name;
        this.isin = isin;
        this.wkn = wkn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getWkn() {
        return wkn;
    }

    public void setWkn(String wkn) {
        this.wkn = wkn;
    }

    public SpotPrice getSpotPrice() {
        return spotPrice;
    }

    public void setSpotPrice(SpotPrice spotPrice) {
        this.spotPrice = spotPrice;
    }

    public String getSpotDate() {
        return spotPrice.getDate();
    }

    public List<SpotPrice> historicalPrices() {
        return new LinkedList<>(historicalPrices);
    }

    public void setHistoricalPrices(List<SpotPrice> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }

    public void update() throws FileNotFoundException {
        String pathname = "FundData/" + this.name + ".txt";
        Scanner input = new Scanner(new File(pathname));
        if (input.next().equals(name)) {
            if (input.next().equals(isin)) {
                if (input.next().equals(wkn)) {
                    String date = input.next();
                    if (spotPrice != null) {
                        if (!spotPrice.getDate().equals(date)) {
                            this.spotPrice = new SpotPrice(input.nextDouble(), date);
                        }
                    } else {
                        this.spotPrice = new SpotPrice(input.nextDouble(), date);
                    }
                    input.close();
                } else {
                    System.out.println("WKN does not match!");
                }
            } else {
                System.out.println("ISIN does not match!");
            }
        } else {
            throw new FileNotFoundException("Datafile " + pathname + " does not match fund " + this.name + "!");
        }
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
        return Objects.hash(name, isin, wkn);
    }
}
