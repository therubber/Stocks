package stocks.dows;

import stocks.interfaces.Security;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SecurityDow implements Security {

    private String name;
    private String isin;
    private String wkn;
    private SpotPrice spotPrice;
    private List<SpotPrice> historicalPrices = new LinkedList<>();

    /**
     * Empty constructor for serialization
     */
    public SecurityDow() {}

    /**
     * Constructor with name -> Needed to select a security in buy orders
     * @param name
     */
    public SecurityDow(String name) {
        this.name = name;
    }

    /**
     * Regular Constructor
     * @param name Name of the security
     * @param isin ISIN of the security
     * @param wkn WKN of the security
     */
    public SecurityDow(String name, String isin, String wkn) {
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

    /**
     * Updates price to most recent one using data from SecurityData files.
     */
    public void update() {
        try {
            updateSpotPrice();
        } catch (FileNotFoundException fnfe) {
            System.out.println("No update file found for " + this.name + ".");
            this.spotPrice = new SpotPrice(0, "UPDATE ERROR");
        }
    }

    private void updateSpotPrice() throws FileNotFoundException {
        String pathname = "SecurityData/" + this.name + "_" + LocalDate.now().toString() + ".txt";
        Scanner input = new Scanner(new File(pathname));
        if (input.next().equals(name)) {
            if (input.next().equals(isin)) {
                if (input.next().equals(wkn)) {
                    String date = input.next();
                    if (!LocalDate.now().toString().equals(date) || spotPrice == null) {
                        this.spotPrice = new SpotPrice(input.nextDouble(), date);
                    }
                } else {
                    System.out.println("WKN does not match!");
                }
            } else {
                System.out.println("ISIN does not match!");
            }
        } else {
            System.out.println("Datafile " + pathname + " does not match security " + this.name + "!");
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
        SecurityDow securityDow = (SecurityDow) o;
        return Objects.equals(name, securityDow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isin, wkn);
    }
}
