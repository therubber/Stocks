package stocks.entities;

import stocks.dows.SecurityDow;
import stocks.dows.SpotPrice;
import stocks.interfaces.Security;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private int count;
    private String id;
    private String executionDate;
    private SecurityDow security;

    /**
     * Empty constructor required for serialization
     */
    public Position() {}

    /**
     * Regular constructor to be used
     * @param count int count of shares contained in the position
     * @param security Security which is object of the position
     */
    public Position(int count, SecurityDow security) {
        this.count = count;
        this.security = security;
        this.id = generateId();
        this.executionDate = LocalDate.now().toString();
    }

    public Position(Order order) {
        this.count = order.getCount();
        this.security = order.getSecurity();
        this.id = generateId();
        this.executionDate = order.getExecutionDate();
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
    public SecurityDow getSecurity() {
        return security;
    }

    /**
     * Setter method for security parameter
     * @param security Security object to set
     */
    public void setSecurity(SecurityDow security) {
        this.security = security;
    }

    /**
     * Getter method to receive value of the position
     * @return Value of the position
     */
    public BigDecimal getValue() {
        return security.getSpotPrice().getPrice().multiply(new BigDecimal(count));
    }

    /**
     * Getter method to receive the ID of the position
     * @return ID of the position
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for ID parameter
     * @param id Id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for spot price of the contained security object
     * @return SpotPrice object of security contained in position
     */
    public SpotPrice getSpotPrice() {
        return security.getSpotPrice();
    }

    /**
     * Getter method for the name of the contained security
     * @return String name of security contained in position
     */
    public String getSecurityName() {
        return security.getName();
    }

    /**
     * Getter method for the executionDate of the position
     * @return String execution date of the order
     */
    public String getExecutionDate() {
        return executionDate;
    }

    /**
     * Setter method for execution date
     * @param executionDate String executionDate to be set for the order
     */
    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Changes the count of the position by the amount of parameter
     * @param count int count that the position is reduced/increased by
     * @return double updated value of the position
     */
    public BigDecimal changeCount(int count) {
        this.count -= count;
        this.executionDate = LocalDate.now().toString();
        return security.getSpotPrice().getPrice().multiply(new BigDecimal(count));
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
        return count == position.count &&
                Objects.equals(security, position.security) &&
                Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, security, id);
    }
}
