package stocks.entities;

import stocks.dows.SecurityDow;
import stocks.interfaces.Security;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Order {

    private int count;
    private BigDecimal executionPrice;
    private String executionDate;
    private boolean type;
    private SecurityDow security;
    private String id;

    /**
     * Default constructor for serialization
     */
    public Order() {}

    /**
     * Constructor for regular use
     * @param executionPrice Spot price at which the order was executed
     * @param executionDate String date at which the order was executed
     * @param type Boolean stating whether the order is Buy/Sell -> default = buy = false
     * @param security Security which is object of the order
     */
    public Order(int count, BigDecimal executionPrice, String executionDate, String type, SecurityDow security) {
        this.count = count;
        this.executionPrice = executionPrice;
        this.executionDate = executionDate;
        if (type.equals("SELL")) {
            this.type = true;
        }
        this.security = security;
        this.id = generateId();
    }

    /**
     * Getter method for count of order
     * @return Int count of order
     */
    public int getCount() {
        return count;
    }

    /**
     * Getter method for execution price
     * @return double execution price
     */
    public BigDecimal getExecutionPrice() {
        return executionPrice;
    }

    /**
     * Getter method for execution date
     * @return String date of execution
     */
    public String getExecutionDate() {
        return executionDate;
    }


    /**
     * Getter method for order type
     * @return String buy/sell depending on type of order
     */
    public String getType() {
        return (!type) ? "BUY" : "SELL";
    }

    /**
     * Getter method for id
     * @return String ID
     */
    public String getId() {
        return id;
    }

    /**
     * Generates an ID for the order, no check for duplicates but unlikely
     * @return String order ID
     */
    public String generateId() {
        return new DecimalFormat("00000000").format((int)(Math.random() * 10000000));
    }

    /**
     * Getter method for security which is object of the order
     * @return Security of order
     */
    public SecurityDow getSecurity() {
        return security;
    }
}
