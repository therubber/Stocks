package stocks.entities;

import stocks.dows.SecurityDow;
import stocks.interfaces.Security;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Portfolio {
    private String name;
    private BigDecimal equity;
    public String owner;
    private List<Position> positions = new LinkedList<>();
    public transient List<SecurityDow> ownedSecurities = new LinkedList<>();
    public List<Order> history = new LinkedList<>();

    /**
     * Empty constructor for serialization
     */
    public Portfolio() {}

    /**
     * Constructor for setting up a portfolio without equity
     * @param name Name
     * @param owner Owner
     */
    public Portfolio(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    /**
     * Specified constructor to generate a fully usable portfolio
     * @param name Name of the portfolio
     * @param owner Owner of the portfolio
     * @param equity Amount of equity allocated to the portfolio
     */
    public Portfolio(String name, String owner, BigDecimal equity) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    /**
     * Getter method to retrieve equity value of the portfolio
     * @return Returns equity available in the portfolio
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * Setter function required for serialization
     * @param equity Double value to set portfolio equity to
     */
    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    /**
     * Adds a position to the portfolio or increase one if it already exists
     */
    public void addPosition(Position position) {
        if (!positions.contains(new Position(position.getSecurity()))) {
            positions.add(position);
        } else {
            positions.get(positions.indexOf(position)).changeCount(position.getCount());
        }
    }

    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Gets a certain position out of the portfolio and returns it. Required to execute sell orders.
     * @param index Index of the position to be returned
     * @return Position from positions at given index
     */
    public Position getPosition(int index) {
        return positions.get(index);
    }

    /**
     * Deletes a position out of the portfolio if count reaches 0
     * @param position Position to be deleted
     */
    public void deletePosition(Position position) {
        positions.remove(position);
    }

    /**
     * Getter method to retrieve the amount of positions in the portfolio. Required for indexing of positions
     * @return int number of positions
     */
    public int getPositionCount() {
        return positions.size();
    }

    /**
     * Displays a list of all securities with data contained in the portfolio
     */
    public void listOwnedSecurities() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-10s %-15s%n", "Name", "ISIN", "WKN", "Price", "Date");
        System.out.println();
        for (Security security : ownedSecurities) {
            System.out.printf("%-18s %-16s %-10s %-10.2f %-15s%n", security.getName(), security.getIsin(), security.getWkn(), security.getSpotPrice().getPrice(), security.getSpotDate());
        }
        System.out.println();
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions() {
        System.out.println();
        System.out.printf("%-9s %10s   %-18s %-10s %-10s%n", "ID", "Count", "Name", "Value", "Execution");
        System.out.println();
        for (Position position : positions) {
            System.out.printf("%-9s %10d   %-18s %-10.2f %-10s%n", position.getId(), position.getCount(), position.getSecurityName(), position.getValue(), position.getExecutionDate());
        }
        System.out.println();
    }

    /**
     * Assigns an index to the positions and prints them out. Required for position selection when selling
     */
    public void indexPositions() {
        System.out.println();
        System.out.printf("%-7s %-9s %10s   %-18s %-10s %-10s%n", "Index", "ID", "Count", "Name", "Value", "Execution");
        System.out.println();
        int i = 0;
        for (Position position : positions) {
            System.out.printf("%-7d %-9s %10d   %-18s %-10.2f %-10s%n", i + 1, position.getId(), position.getCount(), position.getSecurityName(), position.getValue(), position.getExecutionDate());
            i++;
        }
        System.out.println();
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
        BigDecimal value = new BigDecimal(0);
        for (Position position : positions) {
            value = value.add(position.getValue());
        }
        return value;
    }

    /**
     *  Outputs an overview of portfolio data containing:
     *  - combined value of positions
     *  - equity available in portfolio
     *  - combined value of all assets
     */
    public void overview() {
        listOwnedSecurities();
        positions();
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ",  getPositionValue());
        System.out.printf(format, "Equity currently available in portfolio: ", getEquity());
        System.out.printf(format, "Combined value of all assets: ", getValue());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(name, portfolio.name) &&
                Objects.equals(owner, portfolio.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
