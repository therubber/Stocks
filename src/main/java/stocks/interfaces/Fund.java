package stocks.interfaces;

import stocks.dows.SpotPrice;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public interface Fund {
    String getName();
    String getIsin();
    String getWkn();
    double getSpotPrice();
    LinkedList<SpotPrice> histPrices();
    void update() throws FileNotFoundException;
    String toString();
}