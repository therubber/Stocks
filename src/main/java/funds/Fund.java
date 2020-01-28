package funds;

import java.util.LinkedList;

public interface Fund {
    String getName();
    String getISIN();
    int getWKN();
    double getCurrentPrice();
    void setCurrentPrice(double currentPrice);
    LinkedList<Double> histPrice();
    void setHistPrice(LinkedList<Double> histPrice);
}