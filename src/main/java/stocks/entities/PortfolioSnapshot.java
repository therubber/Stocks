package stocks.entities;

import stocks.factories.NumberFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PortfolioSnapshot implements Iterable<Position> {

    private String name;
    public String owner;
    BigDecimal equity;
    List<Position> positions = new LinkedList<>();
    LocalDate state;
    private final NumberFactory factory = new NumberFactory();

    public PortfolioSnapshot(Portfolio portfolio) {
        this.name = portfolio.getName();
        this.owner = portfolio.getOwner();
        this.state = portfolio.state;
        positions.addAll(portfolio.positions);
        this.equity = portfolio.getEquity();
    }

    /**
     * Constructor for searching history for a portfolio of a certain state
     * @param state String date state of the portfolio
     */
    public PortfolioSnapshot(String state) {
        this.state = LocalDate.parse(state);
    }

    /**
     * Getter method to retrieve equity value of the portfolio
     *
     * @return Returns equity available in the portfolio
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * Calculates the current overall value of all assets in the portfolio
     * @return Value of all positions and equity
     */
    public BigDecimal getValue() {
        return getPositionValue().add(equity);
    }

    /**
     * Method to calculate and return combined value of all positions currently in the portfolio
     * @return double value of open positions combined
     */
    public BigDecimal getPositionValue() {
        BigDecimal value = factory.createBigDecimal(0);
        for (Position position : positions) {
            value = value.add(position.getValue());
        }
        return value;
    }

    /**
     * Makes class Portfolio iterable over positions list
     * @return Iterator over positions list
     */
    public Iterator<Position> iterator() {
        return positions.iterator();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioSnapshot portfolio = (PortfolioSnapshot) o;
        return Objects.equals(name, portfolio.name) &&
                Objects.equals(owner, portfolio.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
