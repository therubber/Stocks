package stocks.entities;

import stocks.dows.FundDow;

import java.util.Objects;

public class Position {

    private int count;
    private FundDow fund;
    private double value;
    private String id;

    public Position(int count, FundDow fund) {
        this.count = count;
        this.fund = fund;
        this.value = count * fund.getSpotPrice();
        this.id = "POS00" + count + (int)value * count;
    }

    public int getCount() {
        return count;
    }

    public double getValue() {
        return count * fund.getSpotPrice();
    }

    public String getId() {
        return id;
    }

    public String getFundName() {
        return fund.getName();
    }

    @Override
    public String toString() {
        return count +"\t" + fund.getName() + "\t" + fund.getIsin() + "\t" + fund.getWkn() + "\t" + getValue() + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return count == position.count &&
                Double.compare(position.value, value) == 0 &&
                Objects.equals(fund, position.fund) &&
                Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, fund, value, id);
    }
}
