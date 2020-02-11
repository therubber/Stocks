package stocks.interfaces;

import stocks.dows.SpotPrice;

import java.time.LocalDate;
import java.util.List;

public interface Security {

    /**
     * NOT CURRENTLY IN USE: PROBLEMS WITH SERIALIZATION BECAUSE INTERFACES DONT HAVE NO ARGS CONSTRUCTOR
     */

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

    /**
     * Getter method for Date of spot price
     * @return LocalDate of current spot price
     */
    LocalDate getSpotDate();

    /**
     * Getter method for historical price list
     * @return List of price history
     */
    List<SpotPrice> getPrices();

    /**
     * Setter method for historical price list
     * @param histPrices List containing price history
     */
    void setPrices(List<SpotPrice> histPrices);

}