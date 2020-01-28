package stocks;

import java.util.LinkedList;

public interface Fund {
    String getName();
    String getISIN();
    int getWKN();
    double getCurrentPrice();
    LinkedList<Double> histPrice();
}
