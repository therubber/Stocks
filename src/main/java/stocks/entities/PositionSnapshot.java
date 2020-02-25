package stocks.entities;

import stocks.factories.NumberFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PositionSnapshot {

    private int count;
    private String id;
    private LocalDate executionDate;
    private SecuritySnapshot securitySnapshot;
    private final NumberFactory factory = new NumberFactory();

    public PositionSnapshot(Position position) {
        this.count = position.getCount();
        this.id = position.getId();
        this.executionDate = position.getExecutionDate();
        this.securitySnapshot = new SecuritySnapshot(position.getSecurity());
    }

    /**
     * Getter method to receive count parameter
     * @return Count of securities contained in position
     */
    public int getCount() {
        return count;
    }

    /**
     * Getter method to receive security parameter
     * @return Security object of position
     */
    public SecuritySnapshot getSecurity() {
        return securitySnapshot;
    }

    public String getSecurityType() {
        return securitySnapshot.getType();
    }

    /**
     * Getter method to receive value of the position
     * @return Value of the position
     */
    public BigDecimal getValue() {
        return securitySnapshot.getSpotPrice().getPrice().multiply(factory.createBigDecimal(count));
    }

    /**
     * Getter method to receive the ID of the position
     * @return ID of the position
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for spot price of security
     * @return BigDecimal price of security
     */
    public BigDecimal getPrice() {
        return securitySnapshot.getSpotPrice().getPrice();
    }

    /**
     * Getter method for the executionDate of the position
     * @return String execution date of the order
     */
    public LocalDate getExecutionDate() {
        return executionDate;
    }

    @Override
    public String toString() {
        return count + "\t" + securitySnapshot.getName() + "\t" + securitySnapshot.getIsin() + "\t" + securitySnapshot.getWkn() + "\t" + securitySnapshot.getSpotPrice().getPrice() + "\t\t" + executionDate + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionSnapshot position = (PositionSnapshot) o;
        return Objects.equals(securitySnapshot, position.securitySnapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, securitySnapshot, id);
    }
}
