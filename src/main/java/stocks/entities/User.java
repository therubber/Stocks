package stocks.entities;

import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class User {

    private String username;
    private BigDecimal equity;
    private List<Portfolio> portfolios = new LinkedList<>();
    public List<Order> orderHistory = new LinkedList<>();
    private String password;

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     */
    public User(String username) {
        this.equity = new BigDecimal(Integer.toString(10000)).setScale(2, RoundingMode.HALF_UP);
        this.username = username;
    }

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     * @param password Password to set for user
     */
    public User(String username, String password) {
        this.equity = new BigDecimal(Integer.toString(10000)).setScale(2, RoundingMode.HALF_UP);
        this.username = username;
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
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
     * @param portfolio Portfolio to add
     */
    public void addPortfolio(Portfolio portfolio) {
        portfolios.add(portfolio);
    }

    /**
     * Used to add a portfolio to the user
     */
    public String addPortfolio() {
        System.out.println("User equity: " + equity + "EUR");
        System.out.println("Enter a Name for the portfolio you want to create: ");
        String name = Input.stringValue();
        if (!portfolios.contains(new Portfolio(name, username, LocalDate.now()))) {
            System.out.println("Enter the amount of equity to transfer to the portfolio account: ");
            try {
                BigDecimal depotEquity = BigDecimal.valueOf(Input.doubleValue());
                if (depotEquity.doubleValue() <= equity.doubleValue()) {
                    portfolios.add(new Portfolio(name, username, depotEquity));
                    equity = equity.subtract(depotEquity);
                    System.out.println("Depot " + name + " successfully created!");
                } else {
                    System.out.println("Insufficient account equity for depot creation! please try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input invalid. Please enter a double in the format - 0.00 -  ");
            }
        } else {
            System.out.println("A portfolio with this name already exists, please try again.");
        }
        return name;
    }

    /**
     * Getter for a certain portfolio out of the list of the users portfolios
     * @param name String name of the portfolio to get
     * @return Portfolio requested
     */
    public Portfolio getPortfolio(String name) {
        return portfolios.get(portfolios.indexOf(new Portfolio(name, username, LocalDate.now())));
    }

    /**
     * Method to check whether a user owns a portfolio with the name of the parameter
     * @param name String name of the portfolio
     * @return Boolean whether the portfolio is contained in the users portfolio list
     */
    public boolean hasPortfolio(String name) {
        return portfolios.contains(new Portfolio(name, username, LocalDate.now()));
    }

    /**
     * Checks whether the user has two portfolios (for comparison)
     * @return boolean whether the user has two or more portfolios
     */
    public boolean hasPortfolios() {
        return portfolios.size() >= 2;
    }

    /**
     * Displays a list of all portfolios owned and their current value
     */
    public void listPortfolios() {
        System.out.printf("%-15s %-10s%n", "Name", "Value");
        if (!portfolios.isEmpty()) {
            for (Portfolio portfolio : portfolios) {
                System.out.printf("%-15s %-10.2f%n", portfolio.toString(), portfolio.getValue());
            }
        } else {
            System.out.println("No portfolios available. Please add a new one!");
        }
    }

    /**
     * Prints the order history of the user
     */
    public void printOrderHistory() {
        if (!orderHistory.isEmpty()) {
            System.out.printf("%-8s %10s %5s %15s %10s %15s%n", "ID", "Type", "Count", "Name", "Price", "Date");
            System.out.println();
            for (Order order : orderHistory) {
                System.out.printf("%-8s %10s %5d %15s %10.2f %15s%n", order.getId(), order.getType(), order.getCount(), order.getSecurity().getName(), order.getExecutionPrice(), order.getExecutionDate());
            }
            System.out.println();
        } else {
            System.out.println("No orders yet! Please add a position to your portfolio.");
        }
    }

    /**
     * Compares two portfolios in terms of gains made
     */
    public void compare() {
        listPortfolios();
        System.out.println("Enter the name of the first Portfolio: ");
        Portfolio portfolio1 = new Portfolio(Input.stringValue(), username, LocalDate.now());
        if (portfolios.contains(portfolio1)) {
            System.out.println("Enter the name of the second Portfolio: ");
            Portfolio portfolio2 = new Portfolio(Input.stringValue(), username, LocalDate.now());
            if (portfolios.contains(portfolio2)) {
                comparePortfolios(portfolio1, portfolio2);
            } else {
                System.out.println("Portfolio doesn't exist. Try again:");
            }
        } else {
            System.out.println("Portfolio doesnt exist. Try again:");
        }
    }

    private void comparePortfolios(Portfolio portfolio1, Portfolio portfolio2) {
        Portfolio one = portfolios.get(portfolios.indexOf(portfolio1));
        Portfolio two = portfolios.get(portfolios.indexOf(portfolio2));
        BigDecimal gainOne = one.getValue().subtract(one.getStartEquity());
        BigDecimal gainTwo = two.getValue().subtract(two.getStartEquity());
        System.out.println("Portfolio " + one.getName() + "has gained " + gainOne.toString() + "EUR in value ");
        System.out.println("and is up " + gainOne.divide(one.getStartEquity(), 2, RoundingMode.HALF_UP)+ "%");
        System.out.println("Portfolio " + two.getName() + "has gained " + gainTwo.toString() + "EUR in value.");
        System.out.println("and is up " + gainTwo.divide(two.getStartEquity(), 2,  RoundingMode.HALF_UP) + "%");
    }

    /**
     * Updates all portfolios owned by the user to the most recent prices
     */
    public void updatePortfolios(SecurityRepo securityRepo) {
        for (Portfolio portfolio : portfolios) {
            portfolio.update(securityRepo);
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
        String inputEncoded = Base64.getEncoder().encodeToString(input.getBytes());
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