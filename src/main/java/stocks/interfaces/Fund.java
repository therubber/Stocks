package stocks.interfaces;

import java.util.LinkedList;

public interface Fund {
    String getName();
    String getIsin();
    String getWkn();
    double getSpotPrice();
    LinkedList<Double> histPrices();
    void setHistPrices(LinkedList<Double> histPrice);
    String toString();
}