package stocks.entities;

import stocks.interfaces.Security;

import java.text.DecimalFormat;

public class Order {

    private int count;
    private double executionPrice;
    private String executionDate;
    private boolean type;
    private Security security;
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
    public Order(int count, double executionPrice, String executionDate, String type, Security security) {
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
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter method for count of order
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Getter method for execution price
     * @return double execution price
     */
    public double getExecutionPrice() {
        return executionPrice;
    }

    /**
     * Setter method for execution price
     * @param executionPrice double price to be set
     */
    public void setExecutionPrice(double executionPrice) {
        this.executionPrice = executionPrice;
    }

    /**
     * Getter method for execution date
     * @return String date of execution
     */
    public String getExecutionDate() {
        return executionDate;
    }

    /**
     * Setter method for execution date
     * @param executionDate String date of execution
     */
    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Getter method for order type
     * @return String buy/sell depending on type of order
     */
    public String getType() {
        return (!type) ? "BUY" : "SELL";
    }

    /**
     * Setter method for order type
     * @param type String buy/sell to set order type
     */
    public void setType(String type) {
        if (type.equals("SELL")) {
            this.type = true;
        }
    }

    /**
     * Getter method for id
     * @return String ID
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for id
     * @param id String ID
     */
    public void setId(String id) {
        this.id = id;
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
    public Security getSecurity() {
        return security;
    }

    /**
     * Setter method for security which is object of the order
     * @param security Security to set
     */
    public void setSecurity(Security security) {
        this.security = security;
    }
}
