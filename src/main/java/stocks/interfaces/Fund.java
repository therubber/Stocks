package stocks.interfaces;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public interface Fund {
    String getName();
    String getIsin();
    String getWkn();
    double getSpotPrice();
    LinkedList<Double> histPrices();
    void update() throws FileNotFoundException;
    String toString();
}