package stocks.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {

    private String username;
    private double equity;
    public List<Portfolio> portfolios = new LinkedList<>();

    public User() {}

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

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
