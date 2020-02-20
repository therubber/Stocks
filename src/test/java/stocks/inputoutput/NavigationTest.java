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
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    SecurityRepo securityRepo;
    UserRepo users;

    @BeforeEach
    void setUp() {
        securityRepo.load();
        users.load();
    }

    @Nested
    class usersTest {

        @Test
        void testAdd() {
            users.add(new User("user", "password"));
            assertTrue(users.contains(new User("user", "password")));
        }

        @Test
        @DisplayName("Save / Load")
        void testSaveLoad() {
            users.save();
            users.load();
            assertTrue(users.contains("user"));
        }
    }

    @Nested
    class SecurityRepoTest {

        private Security UniRAK = new Security("UniRAK", "DE0008491044", "849104", "Fund");
        private Security UniAsia = new Security("UniAsia", "LU0037079034", "971267", "Fund");
        private Security UniEuroAnleihen = new Security("UniEuroAnleihen", "LU0966118209", "A1W4QB", "Fund");
        private Security GenoAs = new Security("GenoAs1", "DE0009757682", "975768", "Fund");

        @Test
        void testInitiate() {
            securityRepo.initiate();
            assertTrue(securityRepo.contains(UniRAK));
            assertTrue(securityRepo.contains(UniAsia));
            assertTrue(securityRepo.contains(UniEuroAnleihen));
            assertTrue(securityRepo.contains(GenoAs));
        }

        @Test
        void testIndexOf() {
            assertEquals(0, securityRepo.indexOf(UniRAK));
            assertEquals(1, securityRepo.indexOf(UniEuroAnleihen));
            assertEquals(2, securityRepo.indexOf(UniAsia));
            assertEquals(3, securityRepo.indexOf(GenoAs));
        }

        @Test
        void testGetName() {
            assertEquals(UniRAK, securityRepo.get("UniRAK"));
        }

        @Test
        void testGetIndex() {
            assertEquals(UniRAK, securityRepo.get(0));
        }

        @Test
        void testUpdatePrices() {
            securityRepo.updatePrices();
            assertEquals(new BigDecimal(Double.toString(138.33)).setScale(2, RoundingMode.HALF_UP), securityRepo.get(UniRAK).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(80.59)).setScale(2, RoundingMode.HALF_UP), securityRepo.get(UniAsia).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(57.64)).setScale(2, RoundingMode.HALF_UP), securityRepo.get(UniEuroAnleihen).getSpotPrice().getPrice());
            assertEquals(new BigDecimal(Double.toString(91.68)).setScale(2, RoundingMode.HALF_UP), securityRepo.get(GenoAs).getSpotPrice().getPrice());
        }
    }

    @Test
    void testPortfolio() {
        users.add(new User("testUser", "password"));
        Portfolio portfolio = new Portfolio("test", "testUser", new BigDecimal(5000));
        portfolio.orderInput(new Order(5, LocalDate.now(), "BUY", securityRepo.get("UniRAK")), users);
        assertEquals(new Position(5, securityRepo.get("UniRAK")), portfolio.getPosition(0));
    }
}