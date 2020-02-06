package stocks.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {

    private String username;
    private double equity;
    public List<Portfolio> portfolios = new LinkedList<>();

    /**
     * Empty constructor for serialization
     */
    public User() {}

    /**
     * Regular constructor to be used
     * @param username String username to create user with
     */
    public User(String username) {
        this.equity = 10000.0;
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
     * Setter method for username
     * @param username String username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for equity
     * @return double equity
     */
    public double getEquity() {
        return equity;
    }

    /**
     * Setter method for equity
     * @param equity
     */
    public void setEquity(double equity) {
        this.equity = equity;
    }

    /**
     * Getter method for portfolio list
     * @return List containing all portfolios owned
     */
    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    /**
     * Setter method for portfolios
     * @param portfolios List of portfolios to set
     */
    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
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

    @Override
    public String toString() {
        return username + "   " + equity + "   " + portfolios;
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
