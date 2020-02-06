package stocks.interfaces;

import stocks.dows.SpotPrice;
import java.io.FileNotFoundException;
import java.util.List;

public interface Security {

    /**
     * Getter method for name
     * @return String name of the security
     */
    String getName();

    /**
     * Setter method for name
     * @param name String security name to set
     */
    void setName(String name);

    /**
     * Getter method for ISIN
     * @return String ISIN of the security
     */
    String getIsin();

    /**
     * Setter method for ISIN
     * @param isin String ISIN to set
     */
    void setIsin(String isin);

    /**
     * Getter method for WKN
     * @return String WKN of the security
     */
    String getWkn();

    /**
     * Setter method for WKN
     * @param wkn String WKN to set
     */
    void setWkn(String wkn);

    /**
     * Getter method for spot price
     * @return SpotPrice of the security
     */
    SpotPrice getSpotPrice();

    /**
     * Setter method for spot price
     * @param spotPrice SpotPrice to set
     */
    void setSpotPrice(SpotPrice spotPrice);
    String getSpotDate();

    /**
     * Getter method for historical price list
     * @return List of price history
     */
    List<SpotPrice> historicalPrices();

    /**
     * Setter method for historical price list
     * @param histPrices List containing price history
     */
    void setHistoricalPrices(List<SpotPrice> histPrices);

    /**
     * Updates securitys spot prices if they are not most recent
     * @throws FileNotFoundException If files to fetch data from are in wrong directory, have wrong names or are corrupted.
     */
    void update() throws FileNotFoundException;
}