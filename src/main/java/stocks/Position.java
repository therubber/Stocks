package stocks;

import stocks.funds.Fund;

public class Position {
    private int count;
    private Fund fund;

    public Position(int count, Fund fund) {
        this.count = count;
        this.fund = fund;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder position = new StringBuilder("");
        position.append(count);
        position.append("   ");
        position.append(fund.getName());
        position.append("   ");
        position.append(fund.getISIN());
        position.append("   ");
        position.append(fund.getWKN());
        position.append(System.lineSeparator());
        return position.toString();
    }
}
