package stocks.interfaces;

import stocks.dows.SpotPrice;
import java.io.FileNotFoundException;
import java.util.List;

public interface Fund {
    String getName();
    void setName(String name);
    String getIsin();
    void setIsin(String isin);
    String getWkn();
    void setWkn(String wkn);
    SpotPrice getSpotPrice();
    void setSpotPrice(SpotPrice spotPrice);
    String getSpotDate();
    List<SpotPrice> historicalPrices();
    void setHistoricalPrices(List<SpotPrice> histPrices);
    void update() throws FileNotFoundException;
    String toString();
}