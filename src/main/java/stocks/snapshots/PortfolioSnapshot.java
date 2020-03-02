package stocks.snapshots;

import javafx.geometry.Pos;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.factories.NumberFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PortfolioSnapshot implements Iterable<PositionSnapshot> {

    private BigDecimal equity;
    private LocalDate state;
    private List<PositionSnapshot> positions = new LinkedList<>();
    private final NumberFactory factory = new NumberFactory();

    public PortfolioSnapshot(Portfolio portfolio) {
        this.state = portfolio.getState();
        for (Position position : portfolio.getPositions()) {
            this.positions.add(new PositionSnapshot(position));
        }
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
     * @return Returns equity available in the portfolio
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * Getter method for state of the snapshot
     * @return LocalDate state of the snapshot
     */
    public LocalDate getState() {
        return state;
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
        for (PositionSnapshot position : positions) {
            value = value.add(position.getValue());
        }
        return value;
    }

    /**
     * Getter method for positions
     * @return List<PositionSnapshot>
     */
    public List<PositionSnapshot> getPositions() {
        return positions;
    }

    /**
     * Makes class Portfolio iterable over positions list
     * @return Iterator over positions list
     */
    public Iterator<PositionSnapshot> iterator() {
        return positions.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioSnapshot portfolio = (PortfolioSnapshot) o;
        return Objects.equals(state, portfolio.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
