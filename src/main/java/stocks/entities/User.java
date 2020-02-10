package stocks.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {

    private String username;
    private BigDecimal equity;
    public List<Portfolio> portfolios = new LinkedList<>();
    public List<Order> orderHistory = new LinkedList<>();

    /**
     * Empty constructor for serialization
     */
    public User() {}

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     */
    public User(String username) {
        this.equity = new BigDecimal(10000).setScale(2, RoundingMode.CEILING);
        this.username = username;
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

    public void orderHistory() {
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