package stocks.entities;

import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.inputoutput.Output;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class Portfolio implements Iterable<Position> {

    private String name;
    public String owner;
    private BigDecimal equity;
    private BigDecimal startEquity;
    List<Position> positions = new LinkedList<>();
    transient SecurityRepo ownedSecurities = new SecurityRepo();
    LocalDate state;
    private final Input input;
    List<PortfolioSnapshotEdit> history = new LinkedList<>();
    private final Output out = new Output();
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();

    /**
     * Constructor for setting up a portfolio without equity
     * @param name  Name
     * @param owner Owner
     */
    public Portfolio(String name, String owner, LocalDate state, Input input) {
        this.name = name;
        this.owner = owner;
        this.state = state;
        this.input = input;
    }

    /**
     * Specified constructor to generate a fully usable portfolio
     * @param name   Name of the portfolio
     * @param owner  Owner of the portfolio
     * @param equity Amount of equity allocated to the portfolio
     */
    public Portfolio(String name, String owner, BigDecimal equity, Input input) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
        this.startEquity = equity;
        this.state = LocalDate.now();
        this.input = input;
    }

    public String getName() {
        return name;
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
     * Getter for startequity
     *
     * @return startequity
     */
    public BigDecimal getStartEquity() {
        return startEquity;
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
        portfolioSnapshot.positions();
        String format = "%-45s %10.2f EUR%n";
        System.out.printf(format, "Combined value of positions: ", portfolioSnapshot.getPositionValue(positions));
        System.out.printf(format, "Equity currently available in portfolio: ", portfolioSnapshot.getEquity());
        System.out.printf(format, "Combined value of all assets: ", portfolioSnapshot.getValue());
        out.println();
    }

    /**
     * Adds a position to the portfolio or increases one if it already exists
     */
    public void orderInput(Order order, UserRepo users) {
        Position position = portfolioFactory.createPosition(order);
        if (!positions.isEmpty()) {
            if (!positions.contains(position)) {
                users.get(owner).addOrderToHistory(order);
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
                    users.get(owner).addOrderToHistory(order);
                }
                if (!ownedSecurities.contains(position.getSecurity())) {
                    ownedSecurities.add(position.getSecurity());
                }
                out.println("Buy order successfully executed! New portfolio equity: " + getEquity().setScale(2, RoundingMode.HALF_UP));
            }
        } else {
            users.get(owner).addOrderToHistory(order);
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

    void loadOwnedSecurities() {
        ownedSecurities = new SecurityRepo();
        for (Position position : positions) {
            position.getSecurity().update();
            ownedSecurities.add(position.getSecurity());
        }
    }

    /**
     * Updates all positions to the most recent prices available and adds current state of Portfolio to history List
     */
    public void update(SecurityRepo securityRepo) {
        if (!state.equals(LocalDate.now())) {
            history.add(new PortfolioSnapshotEdit(this));
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
    public void buy(SecurityRepo securityRepo, UserRepo users) {
        if (securityRepo.evaluateSpotPrices()) {
            Position position = selectionBuy(securityRepo);
            if (position.getValue().doubleValue() <= equity.doubleValue() && !position.getIsin().equals("ERROR")) {
                BigDecimal equityAfterExecution = getEquity().subtract(position.getPrice().multiply(numberFactory.createBigDecimal(position.getCount())));
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", getEquity(), equityAfterExecution);
                if (confirmOrder(position, true)) {
                    Order order = portfolioFactory.createOrder(position.getCount(), "BUY", position.getSecurity());
                    orderInput(order, users);
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

    public Position selectionBuy(SecurityRepo securityRepo) {
        try {
            securityRepo.listIndexed();
            out.println("Depot equity: " + equity + " EUR");
            out.println("Enter the index of the Security that you want to buy: ");
            Security security = securityRepo.get(input.intValue() - 1);
            out.println("Enter the count of shares you want to buy: ");
            int transactionCount = input.intValue();
            return portfolioFactory.createPosition(transactionCount, security);
        } catch (InputMismatchException im) {
            out.println("Please enter an Int value. Try again.");
        }
        return portfolioFactory.createPosition(666, securityFactory.createSecurity("ERROR", "ERROR", "ERROR", "ERROR"));
    }

    /**
     * Used to reduce a position in the selected portfolio
     */
    public void sell(UserRepo users) {
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
                    Position position = portfolioFactory.createPosition(order);
                    if (confirmOrder(position, false)) {
                        orderInput(order, users);
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
     * @param position Position to confirm
     * @param type Boolean type of order, true if "BUY"
     * @return Boolean confirmation of the order
     */
    public boolean confirmOrder(Position position, boolean type) {
        Input betterInput = new Input();
        if (type) {
            System.out.println("Buying " + position.getCount() + " shares of " + position.getSecurity().getName() + " at " + position.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        } else {
            System.out.println("Selling " + position.getCount() + " shares of " + position.getSecurity().getName() + " at " + position.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        }
        String confirm = betterInput.stringValue();
        return confirm.equals("y");
    }

    public void valueDevelopment(String state) {
        PortfolioSnapshotEdit oldState = history.get(history.indexOf(portfolioFactory.createPortfolioSnapshotEdit(state)));
        BigDecimal gain = this.getValue().subtract(oldState.getValue());
        System.out.println("Portfolio gained " + gain + " EUR in Value since " + oldState.state + "!");
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
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(name, portfolio.name) &&
                Objects.equals(owner, portfolio.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
