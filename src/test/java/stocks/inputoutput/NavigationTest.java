package stocks.inputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import stocks.entities.*;
import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    SecurityRepo securityRepo;
    UserRepo users;
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();

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
            users.addUser(new User("user", "password"));
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

        private Security UniRAK = securityFactory.createSecurity("UniRAK", "DE0008491044", "849104", "Fund");
        private Security UniAsia = securityFactory.createSecurity("UniAsia", "LU0037079034", "971267", "Fund");
        private Security UniEuroAnleihen = securityFactory.createSecurity("UniEuroAnleihen", "LU0966118209", "A1W4QB", "Fund");
        private Security GenoAs = securityFactory.createSecurity("GenoAs1", "DE0009757682", "975768", "Fund");

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
            assertEquals(numberFactory.createBigDecimal(138.33), securityRepo.get(UniRAK).getSpotPrice().getPrice());
            assertEquals(numberFactory.createBigDecimal(80.59), securityRepo.get(UniAsia).getSpotPrice().getPrice());
            assertEquals(numberFactory.createBigDecimal(57.64), securityRepo.get(UniEuroAnleihen).getSpotPrice().getPrice());
            assertEquals(numberFactory.createBigDecimal(91.68), securityRepo.get(GenoAs).getSpotPrice().getPrice());
        }
    }

    @Test
    void testPortfolio() {
        users.addUser(new User("testUser", "password"));
        Portfolio portfolioSnapshot = new Portfolio("test", "testUser", numberFactory.createBigDecimal(5000), new Input());
        portfolioSnapshot.orderInput(portfolioFactory.createOrder(5, "BUY", securityRepo.get("UniRAK")), users);
        assertEquals(portfolioFactory.createPosition(5, securityRepo.get("UniRAK")), portfolioSnapshot.getPosition(0));
    }
}