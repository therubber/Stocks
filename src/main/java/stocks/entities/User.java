package stocks.entities;

import stocks.repo.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class User {

    private String username;
    private BigDecimal equity;
    private List<Portfolio> portfolios = new LinkedList<>();
    public List<Order> orderHistory = new LinkedList<>();

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     */
    public User(String username) {
        this.equity = new BigDecimal(10000).setScale(2, RoundingMode.CEILING);
        this.username = username;
        Users.add(this);
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
     * Getter method for order history
     * @return List with order history
     */
    public List<Order> getOrderHistory() {
        return orderHistory;
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
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        if (!portfolios.contains(new Portfolio(name, username))) {
            System.out.println("Enter the amount of equity to transfer to the portfolio account: ");
            BigDecimal depotEquity = BigDecimal.valueOf(scanner.nextDouble());
            if (depotEquity.doubleValue() <= equity.doubleValue()) {
                portfolios.add(new Portfolio(name, username, depotEquity));
                equity = equity.subtract(depotEquity);
                System.out.println("Depot " + name + " successfully created!");
            } else {
                System.out.println("Insufficient account equity for depot creation! please try again");
            }
        } else {
            System.out.println("A portfolio with this name already exists, please try again.");
        }
        return name;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
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

    public void printOrderHistory() {
        if (!orderHistory.isEmpty()) {
            System.out.printf("%-8s %10s %5s %15s %10s %15s%n", "ID", "Type", "Count", "Name", "Price", "Date");
            System.out.println();
            for (Order order : orderHistory) {
                System.out.printf("%-8s %10s %5d %15s %10.2f %15s%n", order.getId(), order.getType(), order.getCount(), order.getSecurity().getName(), order.getExecutionPrice(), order.getExecutionDate());
            }
        } else {
            System.out.println("No orders yet! Please add a position to your portfolio.");
        }
    }

    public void compare() {
        listPortfolios();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the first Portfolio: ");
        String portfolio1 = scanner.next();
        if (portfolios.contains(new Portfolio(portfolio1, username))) {
            System.out.println("Enter the name of the second Portfolio: ");
            String portfolio2 = scanner.next();
            if (portfolios.contains(new Portfolio(portfolio2, username))) {
                comparePortfolios(portfolio1, portfolio2);
            } else {
                System.out.println("Portfolio doesn't exist. Try again:");
                compare();
            }
        } else {
            System.out.println("Portfolio doesnt exist. Try again:");
            compare();
        }
    }

    public void comparePortfolios(String portfolio1, String portfolio2) {
        Portfolio one = portfolios.get(portfolios.indexOf(new Portfolio(portfolio1, username)));
        Portfolio two = portfolios.get(portfolios.indexOf(new Portfolio(portfolio2, username)));
        BigDecimal gainOne = one.getValue().subtract(one.getStartEquity());
        BigDecimal gainTwo = two.getValue().subtract(two.getStartEquity());
        System.out.println("Portfolio " + one.getName() + "has gained " + gainOne.toString() + "EUR in value.");
        System.out.println("Portfolio " + two.getName() + "has gained " + gainTwo.toString() + "EUR in value.");
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