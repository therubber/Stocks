package stocks.entities;

import java.util.LinkedList;
import java.util.Objects;

public class User {
    public int key;
    public String username;
    private double equity;
    public LinkedList<Depot> depots = new LinkedList<>();

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
    }

    public double getEquity() {
        return equity;
    }

    public void listDepots() {
        for(Depot depot : depots) {
            System.out.println(depot.toString() + ": " + depot.getEquity() + " EUR, ");
        }
    }

    @Override
    public String toString() {
        return key + "   " +  username + "   " + equity + "   " + depots;
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
