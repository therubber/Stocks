package stocks.entities;

import stocks.factories.NumberFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class Order {

    private int count;
    private SpotPrice executionPrice;
    private boolean type;
    private Security security;
    private String id;
    private final NumberFactory factory = new NumberFactory();

    /**
     * Constructor for regular use
     * @param type Boolean stating whether the order is Buy/Sell -> default = buy = false
     * @param security Security which is object of the order
     */
    public Order(int count, String type, Security security) {
        this.count = count;
        this.executionPrice = security.getSpotPrice();
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
        return executionPrice.getPrice();
    }

    /**
     * Getter method for execution date
     * @return String date of execution
     */
    public LocalDate getExecutionDate() {
        return executionPrice.getDate();
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
     * Getter method for security which is object of the order
     * @return Security of order
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * Gets the combined value of an order
     * @return BigDecimal value of the order
     */
    public BigDecimal getValue() {
        return security.getPrice().multiply(factory.createBigDecimal(count));
    }

    /**
     * Generates an ID for the order, no check for duplicates but unlikely
     * @return String order ID
     */
    private String generateId() {
        return new DecimalFormat("00000000").format((int)(Math.random() * 10000000));
    }
}
