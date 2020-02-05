package stocks.entities;

import java.util.LinkedList;
import java.util.Objects;

public class Portfolio {
    private String name;
    public User owner;
    private LinkedList<Position> positions = new LinkedList<>();     // List with all Positions
    private double equity;                                          // Cash value

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
     *
     * @return Returns equity value of the portfolio
     */
    public double getEquity() {
        return equity;
    }

    /**
     * Sets the equity value of the portfolio
     * @param equity Equity value of the portfolio
     */
    public void changeEquity(double equity) {
        this.equity += equity;
    }

    /** Adds a position to the portfolio */
    public void addPosition(Position position) {
        positions.add(position);
    }

    public Position getPosition(int index) {
        return positions.get(index);
    }

    public void deletePosition(Position position) {
        positions.remove(position);
    }

    public int getPositionCount() {
        return positions.size();
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions() {
        System.out.println();
        System.out.printf("%-9s %10s   %-18s %-10s %-10s%n", "ID", "Count", "Name", "Value", "Execution");
        for (Position position : positions) {
            System.out.printf("%-9s %10d   %-18s %-10.2f %-10s%n", position.getId(), position.getCount(), position.getFundName(), position.getValue(), position.getExecution());
        }
        System.out.println();
    }

    public void indexPositions() {
        System.out.println();
        System.out.printf("%-7s %-9s %10s   %-18s %-10s %-10s%n", "Index", "ID", "Count", "Name", "Value", "Execution");
        int i = 0;
        for (Position position : positions) {
            System.out.printf("%-7d %-9s %10d   %-18s %-10.2f %-10s%n", i + 1, position.getId(), position.getCount(), position.getFundName(), position.getValue(), position.getExecution());
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

    public double getPositionValue() {
        double value = 0;
        for (Position position : positions) {
            value += position.getValue();
        }
        return value;
    }

    /** Outputs a detailed overview of portfolio data */
    public void overview() {
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
