package stocks.inputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import stocks.entities.*;
import stocks.utility.Factory;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    SecurityRepo securityRepo;
    UserRepo users;
    private final Factory factory = new Factory();

    @BeforeEach
    void setUp() {
        securityRepo = new SecurityRepo();
        securityRepo.load();
        users = new UserRepo();
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
            assertTrue(users.contains(new User("user")));
        }
    }

    @Nested
    class SecurityRepoTest {

        private Security UniRAK = factory.createSecurity("UniRAK", "DE0008491044", "849104", "Fund");
        private Security UniAsia = factory.createSecurity("UniAsia", "LU0037079034", "971267", "Fund");
        private Security UniEuroAnleihen = factory.createSecurity("UniEuroAnleihen", "LU0966118209", "A1W4QB", "Fund");
        private Security GenoAs = factory.createSecurity("GenoAs1", "DE0009757682", "975768", "Fund");

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
            assertEquals(factory.createBigDecimal(138.33), securityRepo.get(UniRAK).getSpotPrice().getPrice());
            assertEquals(factory.createBigDecimal(80.59), securityRepo.get(UniAsia).getSpotPrice().getPrice());
            assertEquals(factory.createBigDecimal(57.64), securityRepo.get(UniEuroAnleihen).getSpotPrice().getPrice());
            assertEquals(factory.createBigDecimal(91.68), securityRepo.get(GenoAs).getSpotPrice().getPrice());
        }
    }

    @Test
    void testPortfolio() {
        users.add(new User("testUser", "password"));
        Portfolio portfolio = new Portfolio("test", "testUser", factory.createBigDecimal(5000), new Input());
        portfolio.orderInput(factory.createOrder(5, LocalDate.now(), "BUY", securityRepo.get("UniRAK")), users);
        assertEquals(factory.createPosition(5, securityRepo.get("UniRAK")), portfolio.getPosition(0));
    }
}