package stocks.entities;

import stocks.inputoutput.Help;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.Users;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Portfolio {

    private String name;
    private BigDecimal equity;
    public String owner;
    private BigDecimal startequity;
    private List<Position> positions = new LinkedList<>();
    public SecurityRepo ownedSecurities = new SecurityRepo();
    public List<Portfolio> history = new LinkedList<>();
    public LocalDate state;

    /**
     * Constructor for setting up a portfolio without equity
     * @param name Name
     * @param owner Owner
     */
    public Portfolio(String name, String owner, LocalDate state) {
        this.name = name;
        this.owner = owner;
        this.state = state;
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
        this.state = LocalDate.now();
    }

    // Copy constructor to for historical view of portfolio
    private Portfolio(Portfolio portfolio) {
        this.name = portfolio.name;
        this.owner = portfolio.owner;
        this.equity = portfolio.equity;
        this.startequity = portfolio.startequity;
        this.positions = portfolio.positions;
        this.ownedSecurities = portfolio.ownedSecurities;
        this.history = portfolio.history;
        this.state = portfolio.state;
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

    public String getState() {
        return state.toString();
    }

    /**
     * Adds a position to the portfolio or increases one if it already exists
     */
    public void orderInput(Order order) {
        Position position = new Position(order);
        if (!positions.isEmpty()) {
            if (!positions.contains(position)) {
                Users.get(owner).addOrderToHistory(order);
                executeOrder(order);
            } else {
                Position orderPosition = positions.get(positions.indexOf(position));
                if (order.getType().equals("BUY")) {
                    orderPosition.setCount(orderPosition.getCount() + order.getCount());
                    equity = equity.subtract(order.getValue());
                } else {
                    orderPosition.setCount(orderPosition.getCount() - order.getCount());
                    if (orderPosition.isZero()) {
                        Position positionToRemove = new Position(order.getSecurity());
                        positions.remove(positionToRemove);
                        ownedSecurities.remove(positionToRemove.getSecurity());
                    }
                    equity = equity.add(order.getValue());
                    Users.get(owner).addOrderToHistory(order);
                }
                if (!ownedSecurities.contains(position.getSecurity())) {
                    ownedSecurities.add(position.getSecurity());
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
            if (!ownedSecurities.contains(order.getSecurity())) {
                ownedSecurities.add(order.getSecurity());
            }
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
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions(Portfolio portfolio) {
        System.out.println();
        System.out.printf("%-12s %-10s %-18s %-16s %-10s %-10s %-10s%n", "ID", "Count", "Name", "Type", "Price", "Value", "Execution");
        System.out.println();
        for (Position position : portfolio.positions) {
            System.out.printf("%-12s %-10d %-18s %-16s %-10.2f %-10.2f %-10s%n", position.getId(), position.getCount(), position.getSecurityName(), position.getSecurityType(), position.getPrice(),  position.getValue(), position.getExecutionDate());
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
        ownedSecurities.clear();
        for (Position position : positions) {
            position.getSecurity().update();
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
    public void overview(Portfolio portfolio) {
        portfolio.positions(portfolio);
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ",  portfolio.getPositionValue());
        System.out.printf(format, "Equity currently available in portfolio: ", portfolio.getEquity());
        System.out.printf(format, "Combined value of all assets: ", portfolio.getValue());
        System.out.println();
    }

    /**
     * Updates all positions to the most recent prices available and adds current state of Portfolio to history List
     */
    public void update(SecurityRepo securityRepo) {
        this.state = LocalDate.now();
        if (!history.contains(new Portfolio(this))) {
            history.add(new Portfolio(this));
        }
        updatePrices(securityRepo);
    }

    private void updatePrices(SecurityRepo securityRepo) {
        loadOwnedSecurities();
        for (Position position : positions) {
            Security positionSecurity = position.getSecurity();
            if (securityRepo.contains(positionSecurity)) {
                Security instanceSecurity = securityRepo.get(securityRepo.indexOf(positionSecurity));
                instanceSecurity.update();
                SpotPrice positionSpotPrice = positionSecurity.getSpotPrice();
                SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                if (!positionSpotPrice.equals(instanceSpotPrice)) {
                    positionSecurity.setSpotPrice(instanceSpotPrice);
                }
            }
        }
    }

    /**
     * Sets prices in the portfolio to those of a certain date
     * @param date Date to which the prices should be set
     */
    public void historical(String date) {
        loadOwnedSecurities();
        Portfolio portfolio = getHistoricalPortfolio(date);
        if (portfolio.name.equals("ERROR")) {
            if (portfolio.history.isEmpty()) {
                System.out.println("Error: Portfolio did not exist at that date.");
            } else {
                System.out.println("Error: Portfolio did not exist at " + portfolio.history.get(0).getState() + ".");
            }
        } else {
            overview(portfolio);
        }
    }

    /**
     * Gets a historical State of the portfolio.
     * @param date Date of which the portfolio should be retrieved
     * @return Portfolio at state of the date
     */
    private Portfolio getHistoricalPortfolio(String date) {
        int portfolioIndex = history.indexOf(new Portfolio(getName(), owner, LocalDate.parse(date)));
        if (portfolioIndex != -1) {
            Portfolio portfolio = history.get(portfolioIndex);
            for (Security security : portfolio.ownedSecurities) {
                security.priceFrom(date);
            }
            return portfolio;
        }
        return new Portfolio("ERROR", "ERROR", LocalDate.parse("1970-01-01"));
    }

    /**
     * Used to buy securities and add the position to the portfolio
     */
    public void buy(SecurityRepo securityRepo) {
        if (securityRepo.evaluateSpotPrices()) {
            Position position = selectionBuy(securityRepo);
            if (position.getValue().doubleValue() <= equity.doubleValue() && !position.getIsin().equals("ERROR")) {
                BigDecimal equityAfterExecution = getEquity().subtract(position.getPrice().multiply(new BigDecimal(Integer.toString(position.getCount()))));
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", getEquity(), equityAfterExecution);
                if (Help.confirmOrder(position, true)) {
                    Order order = new Order(position.getCount(), LocalDate.now(), "BUY", position.getSecurity());
                    orderInput(order);
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

    private Position selectionBuy(SecurityRepo securityRepo) {
        try {
            securityRepo.listIndexed();
            System.out.println("Depot equity: " + equity + " EUR");
            System.out.println("Enter the index of the Security that you want to buy: ");
            Security security = securityRepo.get(Input.intValue() - 1);
            System.out.println("Enter the count of shares you want to buy: ");
            int transactionCount = Input.intValue();
            return new Position(transactionCount, security);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds. Try again.");
        } catch (InputMismatchException im) {
            System.out.println("Please enter an Int value. Try again.");
        }
        return new Position(666, new Security("ERROR", "ERROR", "ERROR", "ERROR"));
    }

    /**
     * Used to reduce a position in the selected portfolio
     */
    public void sell() {
        ownedSecurities.listIndexed();
        System.out.println("Please select the security you want to sell by entering its index.");
        int index = Input.intValue();
        if (index > 0 && index <= ownedSecurities.size()) {
            Security selectedSecurity = ownedSecurities.get(index - 1);
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

    /**
     * Shows what gains have been made in  a certain timeframe
     */
    public void histGains() {
        System.out.println("Please enter a date for the beginning of the timeframe:");
        String start = Input.stringValue();
        Portfolio compare = getHistoricalPortfolio(start);
        BigDecimal gain = getValue().subtract(compare.getValue());
        BigDecimal gainPercent = gain.divide(compare.getStartEquity(),5, RoundingMode.HALF_UP).multiply(new BigDecimal(Integer.toString(100)));
        if (gain.doubleValue() > 0) {
            System.out.println("Portfolio has increased " + gain.toString() + " EUR or " + gainPercent + "% in value");
        } else {
            System.out.println("Portfolio has decreased " + gain.multiply(new BigDecimal(Integer.toString(-1))).toString() + " EUR or " + gainPercent + "% in value");
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
                Objects.equals(owner, portfolio.owner) &&
                Objects.equals(state, portfolio.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
