package stocks.entities;

import stocks.factories.NumberFactory;
import stocks.inputoutput.Output;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PortfolioSnapshotEdit implements Iterable<Position> {

    private String name;
    public String owner;
    BigDecimal equity;
    List<Position> positions = new LinkedList<>();
    LocalDate state;
    private final Output out = new Output();
    private final NumberFactory factory = new NumberFactory();

    public PortfolioSnapshotEdit(Portfolio portfolio) {
        this.name = portfolio.getName();
        this.owner = portfolio.owner;
        this.state = portfolio.state;
        this.positions = portfolio.positions;
        this.equity = portfolio.getEquity();
    }

    /**
     * Constructor for searching history for a portfolio of a certain state
     * @param state String date state of the portfolio
     */
    public PortfolioSnapshotEdit(String state) {
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
     * Gets a certain position out of the portfolio and returns it. Required to execute sell orders.
     *
     * @param index Index of the position to be returned
     * @return Position from positions at given index
     */
    public Position getPosition(int index) {
        return positions.get(index);
    }

    /**
     * Getter method to retrieve the amount of positions in the portfolio. Required for indexing of positions
     * @return int number of positions
     */
    public int getPositionCount() {
        return positions.size();
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions() {
        out.println();
        System.out.printf("%-12s %-10s %-18s %-16s %-10s %-10s %-10s%n", "ID", "Count", "Name", "Type", "Price", "Value", "Execution");
        out.println();
        for (Position position : positions) {
            System.out.printf("%-12s %-10d %-18s %-16s %-10.2f %-10.2f %-10s%n", position.getId(), position.getCount(), position.getSecurityName(), position.getSecurityType(), position.getPrice(), position.getValue(), position.getExecutionDate());
        }
        out.println();
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
     * Outputs an overview of portfolio data containing:
     * - combined value of positions
     * - equity available in portfolio
     * - combined value of all assets
     */
    public void overview(PortfolioSnapshot portfolioSnapshot) {
        portfolioSnapshot.positions();
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ", portfolioSnapshot.getPositionValue(positions));
        System.out.printf(format, "Equity currently available in portfolio: ", portfolioSnapshot.getEquity());
        System.out.printf(format, "Combined value of all assets: ", portfolioSnapshot.getValue());
        out.println();
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
        PortfolioSnapshotEdit portfolio = (PortfolioSnapshotEdit) o;
        return Objects.equals(name, portfolio.name) &&
                Objects.equals(owner, portfolio.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
