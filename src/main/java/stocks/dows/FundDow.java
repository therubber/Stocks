package stocks.dows;

import stocks.interfaces.Fund;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class FundDow implements Fund {

    private String name;
    private String isin;
    private String wkn;
    private double spotPrice;
    private LocalDate spotDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
    private LinkedList<Double> historicalPrices = new LinkedList<>();

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
        return historicalPrices;
    }

    public void update() throws FileNotFoundException {
        String pathname = this.name + ".txt";
        Scanner input = new Scanner(new File(pathname));
        if (input.next().equals(name)) {
            while (input.hasNext()) {
                String updateDate = input.next();
                this.isin = input.next();
                this.wkn = input.next();
                if (!updateDate.equals(spotDate.toString())) {
                    historicalPrices.add(spotPrice);
                    this.spotPrice = input.nextDouble();
                }
            }
        } else {
            throw new FileNotFoundException("Datafile " + pathname + " does not match fund " + this.name + "!");
        }
        input.close();
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
