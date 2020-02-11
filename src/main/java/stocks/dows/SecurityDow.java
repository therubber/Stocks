package stocks.dows;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SecurityDow {

    private String name;
    private String isin;
    private String wkn;
    private transient List<SpotPrice> prices = new LinkedList<>();

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

    public List<SpotPrice> getPrices() {
        return new LinkedList<>(prices);
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
