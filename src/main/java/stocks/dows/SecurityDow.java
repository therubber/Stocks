package stocks.dows;

import stocks.interfaces.Security;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        historicalPrices.add(spotPrice);
    }

    public String getSpotDate() {
        return spotPrice.getDate();
    }

    public List<SpotPrice> getHistoricalPrices() {
        return new LinkedList<>(historicalPrices);
    }

    public void setHistoricalPrices(List<SpotPrice> historicalPrices) {
        this.historicalPrices = historicalPrices;
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
