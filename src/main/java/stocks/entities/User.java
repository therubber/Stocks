package stocks.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    public int key;
    private String username;
    private double equity;
    public List<Portfolio> portfolios = new LinkedList<>();

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
    }

    public double getEquity() {
        return equity;
    }

    public void listPortfolios() {
        System.out.printf("%-15s %-10s%n", "Name", "Value");
        for(Portfolio portfolio : portfolios) {
            System.out.printf("%-15s %-10.2f%n", portfolio.toString(), portfolio.getValue());
        }
    }

    public String getName() {
        return username;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    @Override
    public String toString() {
        return key + "   " +  username + "   " + equity + "   " + portfolios;
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
