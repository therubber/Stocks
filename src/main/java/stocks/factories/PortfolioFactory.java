package stocks.factories;

import stocks.entities.*;
import stocks.inputoutput.Input;
import stocks.snapshots.PortfolioSnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioFactory {

    public Position createPosition(Order order) {
        return new Position(order);
    }

    public Position createPosition(Security security) {
        return new Position(security);
    }

    public Position createPosition(int count, Security security) {
        return new Position(count, security);
    }

    public Portfolio createPortfolio(String name, String owner, LocalDate state, Input input) {
        return new Portfolio(name, owner, state);
    }

    public Portfolio createPortfolio(String name, String owner, BigDecimal equity, Input input) {
        return new Portfolio(name, owner, equity);
    }

    public PortfolioSnapshot createPortfolioSnapshot(String state) {
        return new PortfolioSnapshot(state);
    }

    public PortfolioSnapshot createPortfolioSnapshot(Portfolio portfolio) {
        return new PortfolioSnapshot(portfolio);
    }

    public Order createOrder(int count, String type, Security security) {
        return new Order(count, type, security);
    }
}
