package stocks.factories;

import stocks.entities.Security;
import stocks.entities.SpotPrice;

import java.time.LocalDate;

public class SecurityFactory {

    public Security createSecurity(String name, String isin, String wkn, String type) {
        return new Security(name, isin, wkn, type);
    }

    public Security createSecurity(String name) {
        return new Security(name);
    }

    public SpotPrice createSpotPrice(double value, LocalDate date) {
        return new SpotPrice(value, date);
    }
}
