package stocks.entities;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Objects;

public class Portfolio {
    private String name;
    public User owner;
    private LinkedList<Position> positions = new LinkedList<>();     // List with all Positions
    private double equity;                      // Cash value

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
    public void setEquity(double equity) {
        this.equity = equity;
    }

    /** Adds a position to the portfolio */
    public void addPosition(Position position) {
        positions.add(position);
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions() {
        System.out.println("ID\t\t\t" + "Count\t" + "Name\t\t\t\t" + "Value" + "\t\t\t" + "Execution" + System.lineSeparator());
        for (Position position : positions) {
            System.out.println(position.getId() + "\t" + position.getCount() + "\t\t" + position.getFundName() + "\t\t\t\t" + new DecimalFormat("###,###.00").format(position.getValue()) + "\t\t\t" + position.getExecution());
        }
        System.out.println();
    }

    /**
     * Calculates the current overall value of all assets in the portfolio
     * @return Value of all positions and equity
     */
    public double getValue() {
        double value = 0;
        for (Position position : positions) {
            value += position.getValue();
        }
        return value + equity;
    }

    /** Outputs a detailed overview of portfolio data */
    public void overview() {
        positions();

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
