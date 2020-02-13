package stocks.dows;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SecurityDow {

    private String name;
    private String isin;
    private String wkn;
    private List<SpotPrice> prices = new LinkedList<>();

    /**
     * Constructor with name -> Needed to select a security in buy orders
     * @param name Name to set for security
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

    public String getIsin() {
        return isin;
    }

    public String getWkn() {
        return wkn;
    }

    public SpotPrice getSpotPrice() {
        return !prices.isEmpty() ? prices.get(prices.size() - 1) : new SpotPrice(0, LocalDate.parse("1970-01-01"));
    }

    public void setSpotPrice(SpotPrice spotPrice) {
        prices.add(spotPrice);
    }

    public LocalDate getSpotDate() {
        return getSpotPrice().getDate();
    }

    public boolean emptyPrices() {
        return prices.isEmpty();
    }

    public void priceHistory() {
        if (!emptyPrices()) {
            System.out.printf("%-15s %-10s%n", "Date", "Price");
            for (SpotPrice spotPrice : prices) {
                System.out.printf("%-15s %-10.2f%n", spotPrice.getDate(), spotPrice.getPrice());
            }
        } else {
            System.out.println("No historical prices found, please update prices.");
        }
        System.out.println();
    }

    public void update() {
        try {
            String pathname = "SecurityData/SpotData/" + name + ".csv";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathname));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(";");
                prices.add(new SpotPrice(Double.parseDouble(data[0]), LocalDate.parse(data[1])));
            }
            bufferedReader.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("No Update file found!");
        } catch (IOException e) {
            e.printStackTrace();
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
        SecurityDow securityDow = (SecurityDow) o;
        return Objects.equals(name, securityDow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isin, wkn);
    }
}
