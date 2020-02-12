package stocks.inputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stocks.dows.SecurityDow;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    private Navigation instance;
    List<User> users;
    List<Portfolio> portfolios;
    List<Position> positions;

    @BeforeEach
    void setUp() {
        instance = IO.Load.fromJson();
        instance.test("exit");
        String navCurrent = instance.navigation();
        while (!navCurrent.equals("exit")) {
            if (instance.selectedUser == null) {
                navCurrent = instance.navigation();
            } else {
                navCurrent = instance.userNavigation();
            }
        }
        users = instance.getUsers();
        portfolios = users.get(users.indexOf(new User("user"))).getPortfolios();
        positions = portfolios.get(portfolios.indexOf(new Portfolio("test", "user"))).getPositions();
    }

    @Test
    @DisplayName("User creation")
    void testUserCreation() {
        assertTrue(users.contains(new User("test")));
    }

    @Test
    @DisplayName("Portfolio adding")
    void testAddPortfolio() {
        assertFalse(portfolios.isEmpty());
        assertEquals("test", portfolios.get(0).getName());
    }

    @Test
    @DisplayName("Buy Order")
    void testBuying() {
        assertTrue(positions.isEmpty());
        assertTrue(positions.contains(new Position(10, new SecurityDow("UniRAK"))));
    }

    @Test
    @DisplayName("Sell Order")
    void testSelling() {
        assertTrue(positions.contains(new Position(7, new SecurityDow("UniRAK"))));
    }
}