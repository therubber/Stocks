package stocks.entities;

import stocks.dows.SpotPrice;
import stocks.interfaces.Fund;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private int count;
    private Fund fund;
    private double value;
    private String id;
    private String execution;

    public Position() {}

    public Position(int count, Fund fund) {
        this.count = count;
        this.fund = fund;
        this.id = "POS" + new DecimalFormat("000000").format((int)(Math.random() * 100000));
        this.execution = LocalDate.now().toString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public double getValue() {
        return count * fund.getSpotPrice().getPrice();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpotPrice getSpotPrice() {
        return fund.getSpotPrice();
    }

    public String getFundName() {
        return fund.getName();
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public double changeCount(int count) {
        this.count -= count;
        this.value = getValue();
        this.execution = LocalDate.now().toString();
        return count * fund.getSpotPrice().getPrice();
    }

    @Override
    public String toString() {
        return count + "\t" + fund.getName() + "\t" + fund.getIsin() + "\t" + fund.getWkn() + "\t" + fund.getSpotPrice().getPrice() + "\t\t" + execution + System.lineSeparator();
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
