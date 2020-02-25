package stocks.entities;

import stocks.factories.NumberFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private int count;
    private String id;
    private LocalDate executionDate;
    private Security security;
    private final NumberFactory factory = new NumberFactory();

    public Position(Order order) {
        this.count = order.getCount();
        this.id = generateId();
        this.executionDate = order.getExecutionDate();
        this.security = order.getSecurity();
    }

    public Position(Security security) {
        this.security = security;
    }

    /**
     * Regular constructor to be used
     * @param count int count of shares contained in the position
     * @param security Security which is object of the position
     */
    public Position(int count, Security security) {
        this.count = count;
        this.security = security;
        this.id = generateId();
        this.executionDate = LocalDate.now();
    }

    /**
     * Getter method to receive count parameter
     * @return Count of securities contained in position
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter method for count parameter
     * @param count  Count to set for position
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Getter method to receive security parameter
     * @return Security object of position
     */
    public Security getSecurity() {
        return security;
    }

    public String getSecurityType() {
        return security.getType();
    }

    /**
     * Getter method to receive value of the position
     * @return Value of the position
     */
    public BigDecimal getValue() {
        return security.getSpotPrice().getPrice().multiply(factory.createBigDecimal(count));
    }

    /**
     * Getter method to receive the ID of the position
     * @return ID of the position
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for spot price of the contained security object
     * @return SpotPrice object of security contained in position
     */
    public SpotPrice getSpotPrice() {
        return security.getSpotPrice();
    }

    /**
     * Getter method for spot price of security
     * @return BigDecimal price of security
     */
    public BigDecimal getPrice() {
        return security.getSpotPrice().getPrice();
    }

    /**
     * Getter method for the executionDate of the position
     * @return String execution date of the order
     */
    public LocalDate getExecutionDate() {
        return executionDate;
    }

    /**
     * Changes the count of the position by the amount of parameter
     * @param count int count that the position is reduced/increased by
     */
    public void changeCount(int count) {
        this.count += count;
        this.executionDate = LocalDate.now();
    }

    /**
     * Checks whether the count is zero -> used for deleting empty positions
     * @return boolean whether the position has the count of zero
     */
    public boolean isZero() {
        return count == 0;
    }

    /**
     * Generates an ID for the positions, no check for duplicates but unlikely
     * @return String id to be assigned to the position
     */
    public String generateId() {
        return "POS" + new DecimalFormat("000000").format((int)(Math.random() * 100000));
    }

    @Override
    public String toString() {
        return count + "\t" + security.getName() + "\t" + security.getIsin() + "\t" + security.getWkn() + "\t" + security.getSpotPrice().getPrice() + "\t\t" + executionDate + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(security, position.security);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, security, id);
    }
}
