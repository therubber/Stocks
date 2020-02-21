package stocks.utility;

import stocks.entities.*;
import stocks.inputoutput.Input;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Calls all Constructors important for Mocking and returns the created Object
 */

public class Factory {

    public Position createPosition(Order order) {
        return new Position(order);
    }

    public Position createPosition(Security security) {
        return new Position(security);
    }

    public Position createPosition(int count, Security security) {
        return new Position(count, security);
    }

    public SpotPrice createSpotPrice(double value, LocalDate date) {
        return new SpotPrice(value, date);
    }

    public BigDecimal createBigDecimal(int value) {
        return BigDecimalCreator.fromInteger(value);
    }

    public BigDecimal createBigDecimal(double value) {
        return BigDecimalCreator.fromDouble(value);
    }

    public Order createOrder(int count, LocalDate executionDate, String type, Security security) {
        return new Order(count, executionDate, type, security);
    }

    public Security createSecurity(String name, String isin, String wkn, String type) {
        return new Security(name, isin, wkn, type);
    }

    public Security createSecurity(String name) {
        return new Security(name);
    }

    public File createFile(String path) {
        return new File(path);
    }

    public User createUser(String username) {
        return new User(username);
    }

    public User createUser(String username, String password) {
        return new User(username, password);
    }

    public Portfolio createPortfolio(String name, String owner, LocalDate state, Input input) {
        return new Portfolio(name, owner, state, input);
    }

    public Portfolio createPortfolio(String name, String owner, BigDecimal equity, Input input) {
        return new Portfolio(name, owner, equity, input);
    }

    public static class BigDecimalCreator {

        private BigDecimalCreator() {}

        public static BigDecimal fromDouble(double value) {
            return new BigDecimal(Double.toString(value)).setScale(2, RoundingMode.HALF_UP);
        }

        public static BigDecimal fromInteger(int value) {
            return new BigDecimal(Integer.toString(value));
        }
    }
}
