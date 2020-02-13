package stocks.entities;

import stocks.dows.SecurityDow;
import stocks.dows.SpotPrice;
import stocks.inputoutput.Help;
import stocks.repo.Securities;
import stocks.repo.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Portfolio {

    private String name;
    private BigDecimal equity;
    public String owner;
    private BigDecimal startequity;
    private List<Position> positions = new LinkedList<>();
    public transient List<SecurityDow> ownedSecurities = new LinkedList<>();

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
     * Adds a position to the portfolio or increase one if it already exists
     */
    public void orderInput(Order order) {
        Position position = new Position(order);
        if (!positions.isEmpty()) {
            if (!positions.contains(position)) {
                positions.add(position);
                Users.get(owner).getOrderHistory().add(order);
                if (order.getType().equals("BUY")) {
                    equity = equity.subtract(position.getValue());
                } else {
                    equity = equity.add(position.getValue());
                }
            } else {
                positions.get(positions.indexOf(position)).changeCount(position.getCount());
            }
        } else {
            positions.add(position);
            Users.get(owner).getOrderHistory().add(order);
        }
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
        for (SecurityDow security : ownedSecurities) {
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

    public void setOwnedSecurities() {
        ownedSecurities = new LinkedList<>();
        for (Position position : positions) {
            ownedSecurities.add(position.getSecurity());
        }
    }

    public boolean contains(String name) {
        return ownedSecurities.contains(new SecurityDow(name));
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

    public void update() {
        setOwnedSecurities();
        for (Position position : positions) {
            SecurityDow positionSecurity = position.getSecurity();
            if (Securities.contains(positionSecurity)) {
                SecurityDow instanceSecurity = Securities.get(Securities.indexOf(positionSecurity));
                SpotPrice positionSpotPrice = positionSecurity.getSpotPrice();
                SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                if (!positionSpotPrice.equals(instanceSpotPrice)) {
                    positionSecurity.setSpotPrice(instanceSpotPrice);
                }
            }
        }
        for (SecurityDow security : ownedSecurities) {
            if (Securities.contains(security)) {
                SecurityDow instanceSecurity = Securities.get(Securities.indexOf(security));
                SpotPrice positionSpotPrice = security.getSpotPrice();
                SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                if (!positionSpotPrice.equals(instanceSpotPrice)) {
                    security.setSpotPrice(instanceSpotPrice);
                }
            }
        }
    }

    public void buy() {
        if (Securities.evaluateSpotPrices()) {
            Securities.list();
            System.out.println("Depot equity: " + equity + " EUR");
            System.out.println("Enter the name of the Security that you want to buy: ");
            Scanner scanner = new Scanner(System.in);
            String securityName = scanner.next();
            if (!Securities.getAll().contains(new SecurityDow(securityName))) {
                System.out.println("Security not available, please try again.");
                buy();
            } else {
                SecurityDow security = Securities.get(Securities.indexOf(new SecurityDow(securityName)));
                System.out.println("Enter the count of shares you want to buy: ");
                int transactionCount = scanner.nextInt();
                Position position = new Position(transactionCount, security);
                if (position.getValue().doubleValue() <= equity.doubleValue()) {
                    System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", getEquity(), (getEquity().subtract(position.getValue())));
                    System.out.println("Buying " + transactionCount + " shares of " + security.getName() + " at " + security.getSpotPrice().getPrice().setScale(2, RoundingMode.CEILING) + " EUR Spot. Confirm (y/n)");
                    if (Help.confirmOrder()) {
                        Order order = new Order(transactionCount, LocalDate.now(), "BUY", security);
                        orderInput(order);
                        Users.get(owner).getOrderHistory().add(order);
                        if (!ownedSecurities.contains(security)) {
                            ownedSecurities.add(security);
                        }
                        System.out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.CEILING));
                    } else {
                        System.out.println("Buy order cancelled, back to menu.");
                    }
                } else {
                    System.out.println("Insufficient balance! Please try ordering fewer shares.");
                }
            }
            Users.save();
        } else {
            System.out.println("Spot prices were not updated properly. Please try again later.");
        }
    }

    public void sell() {
        if (Securities.evaluateSpotPrices()) {
            indexPositions();
            System.out.println("Please select the position you want to reduce by entering its index.");
            Scanner scanner = new Scanner(System.in);
            int index = scanner.nextInt();
            if (index <= getPositionCount() && index > 0) {
                Position selectedPosition = getPosition(index - 1);
                System.out.println("Enter the amount of shares you want to reduce/increase the position by");
                int transactionCount = scanner.nextInt();
                if (transactionCount <= selectedPosition.getCount()) {
                    System.out.printf("Current equity: %10.2f EUR. After execution: %10.2f%n", getEquity(), (getEquity().add(selectedPosition.getValue()).subtract(selectedPosition.getSpotPrice().getPrice().multiply(new BigDecimal(transactionCount)))));
                    System.out.println("Selling " + transactionCount + " shares of " + selectedPosition.getSecurityName() + " at " + selectedPosition.getSpotPrice().getPrice().setScale(2, RoundingMode.CEILING) + " EUR Spot. Confirm (y/n)");
                    if (Help.confirmOrder()) {
                        Order order = new Order(transactionCount, LocalDate.now(), "SELL", selectedPosition.getSecurity());
                        selectedPosition.setCount(selectedPosition.getCount() - transactionCount);
                        Users.get(owner).getOrderHistory().add(order);
                        equity = equity.add(order.getExecutionPrice().multiply(new BigDecimal(transactionCount)));
                        System.out.println("Sell order successfully executed! New portfolio equity: " + getEquity());
                        if (selectedPosition.isZero()) {
                            deletePosition(selectedPosition);
                            ownedSecurities.remove(selectedPosition.getSecurity());
                        }
                    } else {
                        System.out.println("Sell order cancelled, back to menu.");
                    }
                } else {
                    System.out.println("Amount of shares to sell exceeds amount of shares owned. Please try again.");
                }
            } else {
                System.out.println("The selected position does not exist, please try again.");
            }
            Users.save();
        } else {
            System.out.println("Spot prices were not updated properly. Please try again later.");
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
