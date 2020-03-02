package stocks.entities;

import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.inputoutput.Output;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.snapshots.PortfolioSnapshot;
import stocks.snapshots.PositionSnapshot;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class Portfolio implements Iterable<Position> {

    private String name;
    private String owner;
    private BigDecimal equity;
    private BigDecimal startEquity;
    private LocalDate state;
    private List<Position> positions = new LinkedList<>();
    List<Order> orderHistory = new LinkedList<>();
    transient SecurityRepo ownedSecurities = new SecurityRepo();
    List<PortfolioSnapshot> portfolioHistory = new LinkedList<>();

    private final Input input = new Input();
    private final Output out = new Output();
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();

    /**
     * Constructor for setting up a portfolio without equity
     * @param name  Name
     * @param owner Owner
     */
    public Portfolio(String name, String owner, LocalDate state) {
        this.name = name;
        this.owner = owner;
        this.state = state;
    }

    /**
     * Specified constructor to generate a fully usable portfolio
     * @param name   Name of the portfolio
     * @param owner  Owner of the portfolio
     * @param equity Amount of equity allocated to the portfolio
     */
    public Portfolio(String name, String owner, BigDecimal equity) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
        this.startEquity = equity;
        this.state = LocalDate.now();
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
     * Getter for startequity
     * @return startequity
     */
    BigDecimal getStartEquity() {
        return startEquity;
    }

    /**
     * Calculates the current overall value of all assets in the portfolio
     * @return Value of all positions and equity
     */
    BigDecimal getValue() {
        return getPositionValue().add(equity);
    }

    /**
     * Gets a certain position out of the portfolio and returns it. Required to execute sell orders.
     * @param index Index of the position to be returned
     * @return Position from positions at given index
     */
    Position getPosition(int index) {
        return positions.get(index);
    }

    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Getter method to retrieve the amount of positions in the portfolio. Required for indexing of positions
     * @return int number of positions
     */
    int getPositionCount() {
        return positions.size();
    }

    /**
     * Getter method for state of portfolio
     * @return LocalDate State of the portfolio
     */
    public LocalDate getState() {
        return state;
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    void listPositions(PortfolioSnapshot portfolioSnapshot) {
        out.println();
        System.out.printf("%-12s %-10s %-18s %-16s %-10s %-10s %-10s%n", "ID", "Count", "Name", "Type", "Price", "Value", "Execution");
        out.println();
        for (PositionSnapshot position : portfolioSnapshot.getPositions()) {
            System.out.printf("%-12s %-10d %-18s %-16s %-10.2f %-10.2f %-10s%n", position.getId(), position.getCount(), position.getSecurity().getName(), position.getSecurityType(), position.getPrice(), position.getValue(), position.getExecutionDate());
        }
        out.println();
    }

    /**
     * Method to calculate and return combined value of all positions currently in the portfolio
     * @return double value of open positions combined
     */
    BigDecimal getPositionValue() {
        BigDecimal value = numberFactory.createBigDecimal(0);
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
        listPositions(portfolioSnapshot);
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ", portfolioSnapshot.getPositionValue());
        System.out.printf(format, "Equity currently available in portfolio: ", portfolioSnapshot.getEquity());
        System.out.printf(format, "Combined value of all assets: ", portfolioSnapshot.getValue());
        out.println();
    }

    /**
     * Adds a position to the portfolio or increases one if it already exists
     */
    void orderInput(Order order) {
        Position position = portfolioFactory.createPosition(order);
        if (!positions.isEmpty()) {
            if (!positions.contains(position)) {
                orderHistory.add(order);
                executeOrder(order);
            } else {
                Position orderPosition = positions.get(positions.indexOf(position));
                if (order.getType().equals("BUY")) {
                    orderPosition.setCount(orderPosition.getCount() + order.getCount());
                    equity = equity.subtract(order.getValue());
                } else {
                    orderPosition.setCount(orderPosition.getCount() - order.getCount());
                    cleanPosition(orderPosition);
                    equity = equity.add(order.getValue());
                    orderHistory.add(order);
                }
                if (!ownedSecurities.contains(position.getSecurity())) {
                    ownedSecurities.add(position.getSecurity());
                }
                out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            orderHistory.add(order);
            executeOrder(order);
        }
    }

    private void executeOrder(Order order) {
        if (order.getType().equals("BUY")) {
            positions.add(portfolioFactory.createPosition(order));
            equity = equity.subtract(order.getValue());
            if (!ownedSecurities.contains(order.getSecurity())) {
                ownedSecurities.add(order.getSecurity());
            }
            out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
        } else {
            if (order.getCount() < getPosition(positions.indexOf(portfolioFactory.createPosition(order.getSecurity()))).getCount()) {
                equity = equity.add(order.getValue());
                out.println("Sell order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
            } else {
                out.println("Cannot sell more than you own. Please try again.");
            }
        }
    }

    private void cleanPosition(Position position) {
        if (position.isZero()) {
            positions.remove(position);
        }
    }

    private void loadOwnedSecurities() {
        ownedSecurities = new SecurityRepo();
        for (Position position : positions) {
            position.getSecurity().update();
            ownedSecurities.add(position.getSecurity());
        }
    }

    /**
     * Updates all positions to the most recent prices available and adds current state of Portfolio to history List
     */
    void update(SecurityRepo securityRepo) {
        if (!state.equals(LocalDate.now())) {
            portfolioHistory.add(new PortfolioSnapshot(this));
        }
        this.state = LocalDate.now();
        updatePrices(securityRepo);
    }

    private void updatePrices(SecurityRepo securityRepo) {
        loadOwnedSecurities();
        for (Position position : positions) {
            Security positionSecurity = position.getSecurity();
            if (ownedSecurities.contains(positionSecurity)) {
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
     * Used to buy securities and add the position to the portfolio
     */
    public void buy(SecurityRepo securityRepo) {
        if (securityRepo.evaluateSpotPrices()) {
            Order order = selectionBuy(securityRepo);
            if (order.getValue().doubleValue() <= equity.doubleValue() && !order.getSecurity().getIsin().equals("ERROR")) {
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", getEquity(), getEquity().subtract(order.getValue()));
                if (confirmOrder(order, true)) {
                    orderInput(order);
                } else {
                    out.println("Buy order cancelled, back to menu.");
                }
            } else {
                out.println("Insufficient balance! Please try ordering fewer shares.");
            }
        } else {
            out.println("Spot prices were not updated properly. Please try again later.");
        }
    }

    /**
     * Used to get amount and Security that are to be bought by the user
     * @param securityRepo SecurityRepo containing available securities
     * @return Position containing
     */
    Order selectionBuy(SecurityRepo securityRepo) {
        try {
            securityRepo.listIndexed();
            out.println("Depot equity: " + equity + " EUR");
            out.println("Enter the index of the Security that you want to buy: ");
            Security security = securityRepo.get(input.intValue() - 1);
            out.println("Enter the count of shares you want to buy: ");
            int transactionCount = input.intValue();
            return portfolioFactory.createOrder(transactionCount, "BUY", security);
        } catch (InputMismatchException im) {
            out.println("Please enter an Int value. Try again.");
        }
        return portfolioFactory.createOrder(666, "BUY", securityFactory.createSecurity("ERROR", "ERROR", "ERROR", "ERROR"));
    }

    /**
     * Used to reduce a position in the selected portfolio
     */
    public void sell() {
        ownedSecurities.listIndexed();
        out.println("Please select the security you want to sell by entering its index.");
        int index = input.intValue();
        if (index > 0 && index <= ownedSecurities.size()) {
            Security selectedSecurity = ownedSecurities.get(index - 1);
            if (ownedSecurities.contains(selectedSecurity)) {
                out.println("Enter the amount of shares you want to reduce the position by: ");
                try {
                    int sellCount = input.intValue();
                    Order order = portfolioFactory.createOrder(sellCount, "SELL", selectedSecurity);
                    if (confirmOrder(order, false)) {
                        orderInput(order);
                    }
                } catch (InputMismatchException e) {
                    out.println("Please enter a valid integer value. Try again.");
                }
            } else {
                out.println("Portfolio does not contain the selected Security.");
            }
        } else {
            out.println("Invalid index. Please try again.");
        }
    }

    /**
     * Used to confirm buy/sell orders
     * @param order Order to confirm
     * @param type Boolean type of order, true if "BUY"
     * @return Boolean confirmation of the order
     */
    private boolean confirmOrder(Order order, boolean type) {
        if (type) {
            System.out.println("Buying " + order.getCount() + " shares of " + order.getSecurity().getName() + " at " + order.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        } else {
            System.out.println("Selling " + order.getCount() + " shares of " + order.getSecurity().getName() + " at " + order.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        }
        String confirm = input.stringValue();
        return confirm.equals("y");
    }

    /**
     * Calculates and displays the value Development of the Portfolio from a given state to the current one
     * @param state String historical state to compare to
     */
    public void valueDevelopment(String state) {
        PortfolioSnapshot oldState = portfolioHistory.get(portfolioHistory.indexOf(portfolioFactory.createPortfolioSnapshot(state)));
        BigDecimal gain = this.getValue().subtract(oldState.getValue());
        System.out.println("Portfolio gained " + gain + " EUR in Value since " + oldState.getState() + "!");
    }

    /**
     * Prints the order history of the user
     */
    public void printOrderHistory() {
        if (!orderHistory.isEmpty()) {
            System.out.printf("%-8s %10s %5s %15s %10s %15s%n", "ID", "Type", "Count", "Name", "Price", "Date");
            out.println();
            for (Order order : orderHistory) {
                System.out.printf("%-8s %10s %5d %15s %10.2f %15s%n", order.getId(), order.getType(), order.getCount(), order.getSecurity().getName(), order.getExecutionPrice(), order.getExecutionDate());
            }
            out.println();
        } else {
            out.println("No orders yet! Please add a position to your portfolio.");
        }
    }

    /**
     * Makes class Portfolio iterable over positions list
     * @return Iterator over positions list
     */
    public Iterator<Position> iterator() {
        return positions.iterator();
    }

    void addPosition(Position position) {
        positions.add(position);
    }

    /**
     * Displays a list of historical snapshots available of the portfolio
     */
    public void listHistory() {
        System.out.println("History of " + name + ":");
        for (PortfolioSnapshot portfolioSnapshot : portfolioHistory) {
            System.out.println(portfolioSnapshot.getState());
        }
    }

    /**
     * Displays overview of a historical state of the portfolio
     * @param state String state of the portfolio to display
     */
    public void viewHistorical(String state) {
        PortfolioSnapshot snapshot = portfolioHistory.get(portfolioHistory.indexOf(portfolioFactory.createPortfolioSnapshot(state)));
        overview(snapshot);
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
