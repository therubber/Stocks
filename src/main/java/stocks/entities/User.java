package stocks.entities;

import java.util.LinkedList;
import java.util.Objects;

public class User {
    public int key;
    private String username;
    private double equity;
    public LinkedList<Portfolio> portfolios = new LinkedList<>();

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
    }

    public double getEquity() {
        return equity;
    }

    public void listDepots() {
        for(Portfolio portfolio : portfolios) {
            System.out.println(portfolio.toString() + ": " + portfolio.getValue() + " EUR, ");
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
