package stocks.inputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import stocks.entities.Security;
import stocks.entities.Order;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.repo.Securities;
import stocks.repo.Users;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    @BeforeEach
    void setUp() {
        Securities.load();
        Users.load();
    }

    @Nested
    class UsersTest {

        @Test
        void testAdd() {
            Users.add(new User("user", "password"));
            assertTrue(Users.contains(new User("user", "password")));
        }

        @Test
        @DisplayName("Save / Load")
        void testSaveLoad() {
            Users.save();
            Users.load();
            assertTrue(Users.contains("user"));
        }
    }

    @Nested
    class SecuritiesTest {

        private Security UniRAK = new Security("UniRAK", "DE0008491044", "849104", "Fund");
        private Security UniAsia = new Security("UniAsia", "LU0037079034", "971267", "Fund");
        private Security UniEuroAnleihen = new Security("UniEuroAnleihen", "LU0966118209", "A1W4QB", "Fund");
        private Security GenoAs = new Security("GenoAs1", "DE0009757682", "975768", "Fund");

        @Test
        void testInitiate() {
            Securities.initiate();
            assertTrue(Securities.contains(UniRAK));
            assertTrue(Securities.contains(UniAsia));
            assertTrue(Securities.contains(UniEuroAnleihen));
            assertTrue(Securities.contains(GenoAs));
        }

        @Test
        void testIndexOf() {
            assertEquals(0, Securities.indexOf(UniRAK));
            assertEquals(1, Securities.indexOf(UniEuroAnleihen));
            assertEquals(2, Securities.indexOf(UniAsia));
            assertEquals(3, Securities.indexOf(GenoAs));
        }

        @Test
        void testGetName() {
            assertEquals(UniRAK, Securities.get("UniRAK"));
        }

        @Test
        void testGetIndex() {
            assertEquals(UniRAK, Securities.get(0));
        }

        @Test
        void testUpdatePrices() {
            Securities.updatePrices();
            assertEquals(new BigDecimal(Double.toString(138.33)).setScale(2, RoundingMode.HALF_UP), Securities.get(UniRAK).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(80.59)).setScale(2, RoundingMode.HALF_UP), Securities.get(UniAsia).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(57.64)).setScale(2, RoundingMode.HALF_UP), Securities.get(UniEuroAnleihen).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(91.68)).setScale(2, RoundingMode.HALF_UP), Securities.get(GenoAs).getSpotPrice().getPrice());
        }
    }

    @Test
    void testPortfolio() {
        Users.add(new User("testUser", "password"));
        Portfolio portfolio = new Portfolio("test", "testUser", new BigDecimal(5000));
        portfolio.orderInput(new Order(5, LocalDate.now(), "BUY", Securities.get("UniRAK")));
        assertEquals(new Position(5, Securities.get("UniRAK")), portfolio.getPosition(0));
    }
}