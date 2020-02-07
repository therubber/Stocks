package stocks.entities;

import stocks.interfaces.Security;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Portfolio {
    private String name;
    private double equity;
    public User owner;
    private List<Position> positions = new LinkedList<>();
    public List<Security> ownedSecurities = new LinkedList<>();
    public List<Order> history = new LinkedList<>();

    /**
     * Empty constructor for serialization
     */
    public Portfolio() {}

    public Portfolio(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    /**
     * Specified constructor to generate a fully usable portfolio
     * @param name Name of the portfolio
     * @param owner Owner of the portfolio
     * @param equity Amount of equity allocated to the portfolio
     */
    public Portfolio(String name, User owner, double equity) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
    }

    /**
     *
     * @return Returns the name of the portfolio
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for name parameter
     * @param name String name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to receive the owner of the portfolio
     * @return User Owner of the portfolio
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Setter method for the owner parameter
     * @param owner User to be set as owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Getter method for position list of the portfolio
     * @return List<Position> List containing all positions
     */
    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Setter method for positions list
     * @param positions List<Position> List to be set as positions list in portfolio
     */
    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    /**
     * Getter method to retrieve equity value of the portfolio
     * @return Returns equity available in the portfolio
     */
    public double getEquity() {
        return equity;
    }

    /**
     * Setter function required for serialization
     * @param equity Double value to set portfolio equity to
     */
    public void setEquity(double equity) {
        this.equity = equity;
    }

    /**
     * Sets the equity value of the portfolio
     * @param equity Equity value of the portfolio
     */
    public void changeEquity(double equity) {
        this.equity += equity;
    }

    /**
     * Adds a position to the portfolio
     */
    public void addPosition(Position position) {
        positions.add(position);
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
     * Formats numbers to fit for use with currency
     * @param toFormat Double number to be formatted
     * @return Formatted input
     */
    public double format(double toFormat) {
        return Math.round(toFormat*1e2)/1e2;
    }

    /**
     * Calculates the current overall value of all assets in the portfolio
     * @return Value of all positions and equity
     */
    public double getValue() {
        return getPositionValue() + equity;
    }

    /**
     * Method to calculate and return combined value of all positions currently in the portfolio
     * @return double value of open positions combined
     */
    public double getPositionValue() {
        double value = 0;
        for (Position position : positions) {
            value += position.getValue();
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
