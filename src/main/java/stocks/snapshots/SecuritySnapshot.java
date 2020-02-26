package stocks.snapshots;

import stocks.entities.Security;
import stocks.entities.SpotPrice;

import java.math.BigDecimal;
import java.util.Objects;

public class SecuritySnapshot {

    private String name;
    private String isin;
    private String wkn;
    public String type;
    private SpotPrice price;

    /**
     * Regular constructor
     * @param security Security to create snapshot from
     */
    public SecuritySnapshot(Security security) {
        this.name = security.getName();
        this.isin = security.getIsin();
        this.wkn = security.getWkn();
        this.type = security.getType();
        security.update();
        this.price = security.getSpotPrice();
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

    public String getType() {
        return type;
    }

    /**
     * Directly gets spot price of security
     * @return BigDecimal spot price
     */
    public BigDecimal getPrice() {
        return price.getPrice();
    }

    /**
     * Gets the most recent price of the security. Sets price to 0 and date to 1970-01-01 if no price is available
     * @return Most recent price available, Price (0, 1970-01-01) if no price is available
     */
    public SpotPrice getSpotPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + "   " + isin + "   " + wkn + "   " + getSpotPrice() + " EUR" + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecuritySnapshot security = (SecuritySnapshot) o;
        return Objects.equals(name, security.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
