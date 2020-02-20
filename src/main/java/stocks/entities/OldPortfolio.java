package stocks.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class OldPortfolio {

    private BigDecimal equity;
    private List<Position> positions = new LinkedList<>();
    private LocalDate state;

    public OldPortfolio(Portfolio portfolio) {
        this.equity = portfolio.getEquity();
        this.state = portfolio.state;
        for (Position position : portfolio) {
            this.positions.add((Position) position);
        }
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public LocalDate getState() {
        return state;
    }

    /**
     * Calculates the current overall value of all assets in the portfolio
     * @return Value of all positions and equity
     */
    public BigDecimal getValue() {
        return getPositionValue().add(equity);
    }

    /**
     * Method to calculate and return combined value of all positions currently in the portfolio
     * @return double value of open positions combined
     */
    public BigDecimal getPositionValue() {
        BigDecimal value = new BigDecimal(Integer.toString(0));
        for (Position position : positions) {
            value = value.add(position.getValue());
        }
        return value;
    }

    /**
     * Displays data of all existing positions in the portfolio to the console
     */
    public void positions(Portfolio portfolio) {
        System.out.println();
        System.out.printf("%-12s %-10s %-18s %-16s %-10s %-10s %-10s%n", "ID", "Count", "Name", "Type", "Price", "Value", "Execution");
        System.out.println();
        for (Position position : positions) {
            System.out.printf("%-12s %-10d %-18s %-16s %-10.2f %-10.2f %-10s%n", position.getId(), position.getCount(), position.getSecurityName(), position.getSecurityType(), position.getPrice(),  position.getValue(), position.getExecutionDate());
        }
        System.out.println();
    }
}

