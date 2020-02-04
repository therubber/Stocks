package stocks.entities;

import stocks.dows.FundDow;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private int count;
    private FundDow fund;
    private double value;
    private String id;
    private LocalDate execution;

    public Position(int count, FundDow fund) {
        this.count = count;
        this.fund = fund;
        this.value = count * fund.getSpotPrice();
        this.id = "POS" + new DecimalFormat("000000").format((int)(Math.random() * 100000));
        this.execution = LocalDate.now();
    }

    public int getCount() {
        return count;
    }

    public double changeCount(int count) {
        this.count -= count;
        this.value = getValue();
        this.execution = LocalDate.now();
        return count * fund.getSpotPrice();
    }

    public double getValue() {
        return count * fund.getSpotPrice();
    }

    public String getId() {
        return id;
    }

    public FundDow getFund() {
        return fund;
    }

    public String getFundName() {
        return fund.getName();
    }

    public String getExecution() {
        return execution.toString();
    }

    @Override
    public String toString() {
        return count + "\t" + fund.getName() + "\t" + fund.getIsin() + "\t" + fund.getWkn() + "\t" + new DecimalFormat("###,###.00").format(getValue()) + "\t\t" + execution.toString() + System.lineSeparator();
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
