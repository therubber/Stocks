package stocks;

import java.util.LinkedList;

public class User {
    public int key;
    public String username;
    private double equity;
    LinkedList<Depot> depots = new LinkedList<Depot>();

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
    }

    public void addDepot(String name, int equity) {
        depots.add(new Depot(name, this, equity));
        this.equity -= equity;
        System.out.println("New depot created! Equity: " + equity);
    }

    @Override
    public String toString() {
        return key + "   " +  username + "   " + equity + "   " + depots;
    }
}
