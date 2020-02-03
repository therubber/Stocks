package stocks.entities;

import java.util.LinkedList;
import java.util.Objects;

public class Depot {
    public String name;
    public User owner;
    private double value;                       // Combined Value of Securities
    private LinkedList<Position> positions = new LinkedList<>();     // List with all Positions
    private double equity;                      // Cash value

    public Depot(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Depot(String name, User owner, double equity) {
        this.name = name;
        this.equity = equity;
        this.owner = owner;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public void addPosition(Position position) {
        positions.add(position);
    }

    public void positions() {
        System.out.println("ID\t\t\t" + "Count\t" + "Name\t\t\t\t" + "Value" + System.lineSeparator());
        for (Position position : positions) {
            System.out.println(position.getId() + "\t" + position.getCount() + "\t\t" + position.getFundName() + "\t\t\t\t" + position.getValue());
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Depot depot = (Depot) o;
        return Objects.equals(name, depot.name) &&
                Objects.equals(owner, depot.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }
}
