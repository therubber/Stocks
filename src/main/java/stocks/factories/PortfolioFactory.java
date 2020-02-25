package stocks.factories;

import stocks.entities.*;
import stocks.inputoutput.Input;

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

    public PortfolioSnapshot createPortfolioSnapshot(String name, String owner, LocalDate state, Input input) {
        return new PortfolioSnapshot(name, owner, state, input);
    }

    public PortfolioSnapshot createPortfolioSnapshot(String name, String owner, BigDecimal equity, Input input) {
        return new PortfolioSnapshot(name, owner, equity, input);
    }

    public PortfolioSnapshotEdit createPortfolioSnapshotEdit(String state) {
        return new PortfolioSnapshotEdit(state);
    }

    public Order createOrder(int count, String type, Security security) {
        return new Order(count, type, security);
    }
}
