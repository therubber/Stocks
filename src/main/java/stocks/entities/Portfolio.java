package stocks.entities;

import stocks.inputoutput.Help;
import stocks.inputoutput.Input;
import stocks.repo.Securities;
import stocks.repo.Users;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Portfolio {

    private String name;
    private BigDecimal equity;
    public String owner;
    private BigDecimal startequity;
    private List<Position> positions = new LinkedList<>();
    public transient List<Security> ownedSecurities = new LinkedList<>();

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
        this.startequity = equity;
    }

    public String getName() {
        return name;
    }

    /**
     * Getter method to retrieve equity value of the portfolio
     * @return Returns equity available in the portfolio
     */
    public BigDecimal getEquity() {
        return equity.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Getter for startequity
     * @return startequity
     */
    public BigDecimal getStartEquity() {
        return startequity;
    }

    /**
     * Adds a position to the portfolio or increases one if it already exists
     */
    public void orderInput(Order order) {
        Position position = new Position(order);
        if (!positions.isEmpty()) {
            if (!positions.contains(position)) {
                positions.add(position);
                Users.get(owner).addOrderToHistory(order);
                executeOrder(order);
            } else {
                Position orderPosition = positions.get(positions.indexOf(position));
                if (order.getType().equals("BUY")) {
                    orderPosition.setCount(orderPosition.getCount() + order.getCount());
                    equity = equity.subtract(order.getValue());
                } else {
                    orderPosition.setCount(orderPosition.getCount() - order.getCount());
                    equity = equity.add(order.getValue());
                }
                System.out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            Users.get(owner).addOrderToHistory(order);
            executeOrder(order);
        }
    }

    private void executeOrder(Order order) {
        if (order.getType().equals("BUY")) {
            positions.add(new Position(order));
            equity = equity.subtract(order.getValue());
        } else {
            if (order.getCount() < getPosition(positions.indexOf(new Position(order.getSecurity()))).getCount()) {
                equity = equity.add(order.getValue());
            } else {
                System.out.println("Cannot sell more than you own. Please try again.");
            }
        }
        System.out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
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
        BigDecimal value = new BigDecimal(Integer.toString(0));
        for (Position position : positions) {
            value = value.add(position.getValue());
        }
        return value;
    }

    void loadOwnedSecurities() {
        ownedSecurities = new LinkedList<>();
        for (Position position : positions) {
            ownedSecurities.add(position.getSecurity());
        }
    }

    /**
     * Checks whether the portfolio contains a position with the String name of the security
     * @param name Name of the security to check for
     * @return Boolean whether a position with the security name exists
     */
    public boolean contains(String name) {
        return ownedSecurities.contains(new Security(name));
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
        BigDecimal gain = getValue().subtract(getStartEquity());
        if (Double.parseDouble(gain.toString()) >= 0) {
            System.out.println("Portfolio " + name + " has increased " + getValue().subtract(getStartEquity()) + " EUR (" + gain.divide(getStartEquity(), RoundingMode.HALF_UP) + "%) in value!");
        } else {
            System.out.println("Portfolio " + name + " has decreased " + getValue().subtract(getStartEquity()) + " EUR (" + gain.divide(getStartEquity(), RoundingMode.HALF_UP) + "%) in value!");
        }
    }

    private void historicalOverview() {
        positions();
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ",  getPositionValue());
        System.out.printf(format, "Equity currently available in portfolio: ", getEquity());
        System.out.printf(format, "Combined value of all assets: ", getValue());
        BigDecimal gain = getValue().subtract(getStartEquity());
        if (Double.parseDouble(gain.toString()) >= 0) {
            System.out.println("Portfolio " + name + " would have decreased " + getValue().subtract(getStartEquity()) + " EUR (" + gain.divide(getStartEquity(), RoundingMode.HALF_UP) + "%) in value had it been started with current positions at the given date.");
        } else {
            System.out.println("Portfolio " + name + " would have increased " + getValue().subtract(getStartEquity()) + " EUR (" + gain.divide(getStartEquity(), RoundingMode.HALF_UP) + "%) in value had it been started with current positions at the given date.");
        }
    }

    /**
     * Updates all positions to the most recent prices available
     */
    public void update() {
        loadOwnedSecurities();
        for (Position position : positions) {
            Security positionSecurity = position.getSecurity();
            if (Securities.contains(positionSecurity)) {
                Security instanceSecurity = Securities.get(Securities.indexOf(positionSecurity));
                SpotPrice positionSpotPrice = positionSecurity.getSpotPrice();
                SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                if (!positionSpotPrice.equals(instanceSpotPrice)) {
                    positionSecurity.setSpotPrice(instanceSpotPrice);
                }
            }
        }
        for (Security security : ownedSecurities) {
            if (Securities.contains(security)) {
                Security instanceSecurity = Securities.get(Securities.indexOf(security));
                SpotPrice positionSpotPrice = security.getSpotPrice();
                SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                if (!positionSpotPrice.equals(instanceSpotPrice)) {
                    security.setSpotPrice(instanceSpotPrice);
                }
            }
        }
    }

    /**
     * Sets prices in the portfolio to those of a certain date
     * @param date Date to which the prices should be set
     */
    public void valueFrom(String date) {
        loadOwnedSecurities();
        for (Security security : ownedSecurities) {
            security.priceFrom(date);
        }
        historicalOverview();
        update();
    }

    /**
     * Used to buy securities and add the position to the portfolio
     */
    public void buy() {
        if (Securities.evaluateSpotPrices()) {
            Position position = selectionBuy();
            if (position.getValue().doubleValue() <= equity.doubleValue() && !position.getIsin().equals("ERROR")) {
                BigDecimal equityAfterExecution = getEquity().subtract(position.getPrice().multiply(new BigDecimal(Integer.toString(position.getCount()))));
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", getEquity(), equityAfterExecution);
                if (Help.confirmOrder(position, true)) {
                    Order order = new Order(position.getCount(), LocalDate.now(), "BUY", position.getSecurity());
                    orderInput(order);
                    if (!ownedSecurities.contains(position.getSecurity())) {
                        ownedSecurities.add(position.getSecurity());
                    }
                } else {
                    System.out.println("Buy order cancelled, back to menu.");
                }
            } else {
                System.out.println("Insufficient balance! Please try ordering fewer shares.");
            }
            Users.save();
        } else {
            System.out.println("Spot prices were not updated properly. Please try again later.");
        }
    }

    private Position selectionBuy() {
        try {
            Securities.listIndexed();
            System.out.println("Depot equity: " + equity + " EUR");
            System.out.println("Enter the index of the Security that you want to buy: ");
            Security security = Securities.get(Input.intValue() - 1);
            System.out.println("Enter the count of shares you want to buy: ");
            int transactionCount = Input.intValue();
            return new Position(transactionCount, security);
        } catch (IndexOutOfBoundsException e) {
            return new Position(666, new Security("ERROR", "ERROR", "ERROR"));
        }
    }

    /**
     * Used to reduce a position in the selected portfolio
     */
    public void sell() {
        Securities.listIndexed();
        System.out.println("Please select the security you want to sell by entering its index.");
        int index = Input.intValue();
        if (index > 0 && index <= Securities.size()) {
            Security selectedSecurity = Securities.get(index - 1);
            if (ownedSecurities.contains(selectedSecurity)) {
                System.out.println("Enter the amount of shares you want to reduce the position by: ");
                int sellCount = Input.intValue();
                Order order = new Order(sellCount, LocalDate.now(), "SELL", selectedSecurity);
                Position position = new Position(order);
                if (Help.confirmOrder(position, false)) {
                    orderInput(order);
                }
            } else {
                System.out.println("Portfolio does not contain the selected Security.");
            }
        } else {
            System.out.println("Invalid index. Please try again.");
        }
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
