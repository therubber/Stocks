package stocks;

import java.util.LinkedList;

public class Depot {
    public String name;
    public User owner;
    private double value;                       // Combined Value of Securities
    private LinkedList<Position> balance;       // List with all Positions
    private double equity;                      // Cash value

    public Depot(String name, User owner, double equity) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name + ", ";
    }
}
