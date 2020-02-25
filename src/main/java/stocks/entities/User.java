package stocks.entities;

import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.inputoutput.Output;
import stocks.utility.Encoder;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class User {

    private String username;
    private BigDecimal equity;
    private List<PortfolioSnapshot> portfolioSnapshots = new LinkedList<>();
    public List<Order> orderHistory = new LinkedList<>();
    private String password;
    private final Input input = new Input();
    private final Output out = new Output();
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     */
    public User(String username) {
        this.equity = numberFactory.createBigDecimal(10000);
        this.username = username;
    }

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     * @param password Password to set for user
     */
    public User(String username, String password) {
        this.equity = numberFactory.createBigDecimal(10000);
        this.username = username;
        this.password = Encoder.encode(password);
    }

    /**
     * Getter method for username
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter method for equity
     * @return double equity
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * Setter method for equity
     * @param equity BigDecimal to set equity to
     */
    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    /**
     * Adding portfolio for testing
     * @param portfolioSnapshot Portfolio to add
     */
    public void addPortfolio(PortfolioSnapshot portfolioSnapshot) {
        portfolioSnapshots.add(portfolioSnapshot);
    }

    /**
     * Used to add a portfolio to the user
     */
    public void addPortfolio() {
        out.println("User equity: " + equity + "EUR");
        out.println("Enter a Name for the portfolio you want to create: ");
        String name = input.stringValue();
        if (!portfolioSnapshots.contains(portfolioFactory.createPortfolioSnapshot(name, username, LocalDate.now(), input))) {
            out.println("Enter the amount of equity to transfer to the portfolio account: ");
            try {
                BigDecimal depotEquity = BigDecimal.valueOf(input.doubleValue());
                if (depotEquity.doubleValue() <= equity.doubleValue()) {
                    PortfolioSnapshot toAdd = portfolioFactory.createPortfolioSnapshot(name, username, depotEquity, input);
                    portfolioSnapshots.add(toAdd);
                    equity = equity.subtract(depotEquity);
                    out.println("Depot " + name + " successfully created!");
                } else {
                    out.println("Insufficient account equity for depot creation! please try again");
                }
            } catch (InputMismatchException e) {
                out.println("Input invalid. Please enter a double in the format - 0.00 -  ");
            }
        } else {
            out.println("A portfolio with this name already exists, please try again.");
        }
    }

    /**
     * Getter for a certain portfolio out of the list of the users portfolios
     * @param name String name of the portfolio to get
     * @return Portfolio requested
     */
    public PortfolioSnapshot getPortfolio(String name) {
        return portfolioSnapshots.get(portfolioSnapshots.indexOf(portfolioFactory.createPortfolioSnapshot(name, username, LocalDate.now(), input)));
    }

    /**
     * Method to check whether a user owns a portfolio with the name of the parameter
     * @param name String name of the portfolio
     * @return Boolean whether the portfolio is contained in the users portfolio list
     */
    public boolean hasPortfolio(String name) {
        return portfolioSnapshots.contains(portfolioFactory.createPortfolioSnapshot(name, username, LocalDate.now(), input));
    }

    /**
     * Checks whether the user has two portfolios (for comparison)
     * @return boolean whether the user has two or more portfolios
     */
    public boolean hasPortfolios() {
        return portfolioSnapshots.size() >= 2;
    }

    /**
     * Displays a list of all portfolios owned and their current value
     */
    public void listPortfolios() {
        System.out.printf("%-15s %-10s%n", "Name", "Value");
        if (!portfolioSnapshots.isEmpty()) {
            for (PortfolioSnapshot portfolioSnapshot : portfolioSnapshots) {
                System.out.printf("%-15s %-10.2f%n", portfolioSnapshot.toString(), portfolioSnapshot.getValue());
            }
        } else {
            out.println("No portfolios available. Please add a new one!");
        }
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
     * Compares two portfolios in terms of gains made
     */
    public void compare() {
        listPortfolios();
        out.println("Enter the name of the first Portfolio: ");
        PortfolioSnapshot portfolioSnapshot1 = portfolioFactory.createPortfolioSnapshot(input.stringValue(), username, LocalDate.now(), input);
        if (portfolioSnapshots.contains(portfolioSnapshot1)) {
            out.println("Enter the name of the second Portfolio: ");
            PortfolioSnapshot portfolioSnapshot2 = portfolioFactory.createPortfolioSnapshot(input.stringValue(), username, LocalDate.now(), input);
            if (portfolioSnapshots.contains(portfolioSnapshot2)) {
                comparePortfolios(portfolioSnapshot1, portfolioSnapshot2);
            } else {
                out.println("Portfolio doesn't exist. Try again:");
            }
        } else {
            out.println("Portfolio doesnt exist. Try again:");
        }
    }

    private void comparePortfolios(PortfolioSnapshot portfolioSnapshot1, PortfolioSnapshot portfolioSnapshot2) {
        PortfolioSnapshot one = portfolioSnapshots.get(portfolioSnapshots.indexOf(portfolioSnapshot1));
        PortfolioSnapshot two = portfolioSnapshots.get(portfolioSnapshots.indexOf(portfolioSnapshot2));
        BigDecimal gainOne = one.getValue().subtract(one.getStartEquity());
        BigDecimal gainTwo = two.getValue().subtract(two.getStartEquity());
        out.println("Portfolio " + one.getName() + "has gained " + gainOne.toString() + "EUR in value ");
        out.println("and is up " + gainOne.divide(one.getStartEquity(), 2, RoundingMode.HALF_UP)+ "%");
        out.println("Portfolio " + two.getName() + "has gained " + gainTwo.toString() + "EUR in value.");
        out.println("and is up " + gainTwo.divide(two.getStartEquity(), 2,  RoundingMode.HALF_UP) + "%");
    }

    /**
     * Updates all portfolios owned by the user to the most recent prices
     */
    public void updatePortfolios(SecurityRepo securityRepo) {
        for (PortfolioSnapshot portfolioSnapshot : portfolioSnapshots) {
            portfolioSnapshot.update(securityRepo);
        }
    }

    /**
     * Adds an order to the order history
     * @param order Order to be added to history
     */
    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    /**
     * Checks if input password and saved passwords match
     * @param input String password input
     * @return boolean validity of password
     */
    public boolean checkPassword(String input) {
        String inputEncoded = Encoder.encode(input);
        return inputEncoded.equals(password);
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}