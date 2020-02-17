package stocks.entities;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Security {

    private String name;
    private String isin;
    private String wkn;
    private List<SpotPrice> prices = new LinkedList<>();

    /**
     * Constructor with name -> Needed to select a security in buy orders
     * @param name Name to set for security
     */
    public Security(String name) {
        this.name = name;
    }

    /**
     * Regular Constructor
     * @param name Name of the security
     * @param isin ISIN of the security
     * @param wkn WKN of the security
     */
    public Security(String name, String isin, String wkn) {
        this.name = name;
        this.isin = isin;
        this.wkn = wkn;
    }

    /**
     * Getter method for name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for ISIN
     * @return String ISIN
     */
    public String getIsin() {
        return isin;
    }

    /**
     * Getter method for WKN
     * @return String WKN
     */
    public String getWkn() {
        return wkn;
    }

    /**
     * Directly gets spot price of security
     * @return BigDecimal spot price
     */
    public BigDecimal getPrice() {
        return prices.get(prices.size() - 1).getPrice();
    }

    /**
     * Gets the most recent price of the security. Sets price to 0 and date to 1970-01-01 if no price is available
     * @return Most recent price available, Price (0, 1970-01-01) if no price is available
     */
    public SpotPrice getSpotPrice() {
        return !prices.isEmpty() ? prices.get(prices.size() - 1) : new SpotPrice(0, LocalDate.parse("1970-01-01"));
    }

    /**
     * Sets the spot price
     * @param spotPrice SpotPrice to be assigned
     */
    public void setSpotPrice(SpotPrice spotPrice) {
        prices.add(spotPrice);
    }

    /**
     * Gets SpotDate of the security, used to check whether the prices are up to date.
     * @return SpotDate of the most recent SpotPrice
     */
    public LocalDate getSpotDate() {
        return getSpotPrice().getDate();
    }

    /**
     * Checks whether a price is allocated to the security
     * @return Boolean if price is allocated
     */
    public boolean emptyPrices() {
        return prices.isEmpty();
    }

    /**
     * Displays price history
     */
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

    /**
     * Updates securities SpotPrice from SpotData files
     */
    public void update() {
        try {
            File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("SecurityData/" + name + ".csv")).getFile());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
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

    /**
     * Gets the Spotprice of the Security from a certain date
     * @param date Date of which a spot price should be returned
     */
    public void priceFrom(String date) {
        SpotPrice histPrice = new SpotPrice(0.0, LocalDate.parse("1970-01-01"));
        for (SpotPrice price : prices) {
            if (price.getDate().equals(LocalDate.parse(date))) {
                histPrice = price;
            }
        }
        if (histPrice.getDate().equals(LocalDate.parse("1970-01-01"))) {
            System.out.println("No price data found matching input date! Please try again");
        } else {
            prices.add(histPrice);
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
        Security security = (Security) o;
        return Objects.equals(name, security.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isin, wkn);
    }
}
