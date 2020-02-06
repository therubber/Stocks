package stocks.entities;

import stocks.dows.SpotPrice;
import stocks.interfaces.Security;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private int count;
    private Security security;
    private String id;
    private String executionDate;

    /**
     * Empty constructor required for serialization
     */
    public Position() {}

    /**
     * Regular constructor to be used
     * @param count int count of shares contained in the position
     * @param security Security which is object of the position
     */
    public Position(int count, Security security) {
        this.count = count;
        this.security = security;
        this.id = "POS" + new DecimalFormat("000000").format((int)(Math.random() * 100000));
        this.executionDate = LocalDate.now().toString();
    }

    /**
     * Getter method to receive count parameter
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter method for count parameter
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Getter method to receive security parameter
     * @return
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * Setter method for security parameter
     * @param security
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Getter method to receive value of the position
     * @return
     */
    public double getValue() {
        return count * security.getSpotPrice().getPrice();
    }

    /**
     * Getter method to receive the ID of the position
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for ID parameter
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for spot price of the contained security object
     * @return
     */
    public SpotPrice getSpotPrice() {
        return security.getSpotPrice();
    }

    /**
     * Getter method for the name of the contained security
     * @return
     */
    public String getSecurityName() {
        return security.getName();
    }

    /**
     * Getter method for the executionDate of the position
     * @return
     */
    public String getExecutionDate() {
        return executionDate;
    }

    /**
     * Setter method for execution date
     * @param executionDate
     */
    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Changes the count of the position by the amount of parameter
     * @param count int count that the position is reduced/increased by
     * @return double updated value of the position
     */
    public double changeCount(int count) {
        this.count -= count;
        this.executionDate = LocalDate.now().toString();
        return count * security.getSpotPrice().getPrice();
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
