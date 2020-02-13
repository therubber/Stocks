package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.repo.Securities;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @BeforeEach
    void setUp() {
        Securities.initiate();
        Securities.updatePrices();
        User user = new User("user");
    }
    @Test
    void getUsername() {

    }

    @Test
    void getEquity() {
    }

    @Test
    void setEquity() {
    }

    @Test
    void getOrderHistory() {
    }

    @Test
    void getPortfolios() {
    }

    @Test
    void listPortfolios() {
    }

    @Test
    void printOrderHistory() {
    }

    @Test
    void compare() {
    }

    @Test
    void comparePortfolios() {
    }
}