package stocks.inputoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import stocks.entities.*;
import stocks.factories.NumberFactory;
import stocks.factories.SecurityFactory;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    SecurityRepo securityRepo;
    UserRepo users;
    private final NumberFactory numberFactory = new NumberFactory();
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
        @DisplayName("Save / Load")
        void testSaveLoad() {
            users.save();
            users.load();
            // assertTrue(users.contains(new User("user", new Input(), new Output())));
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

    /* @Test
    void selectPortfolio() {
        Navigation navigation = new Navigation();
        User userMock = Mockito.mock(User.class);
        Input inputMock = Mockito.mock(Input.class);
        String depotName = "schnitzel";
        Portfolio portfolio = new Portfolio(depotName, "jay unit", numberFactory.createBigDecimal(5));

        doReturn(depotName).when(inputMock).stringValue();
        doReturn(true).when(userMock).hasPortfolio(depotName);
        doReturn(portfolio).when(userMock).getPortfolio(depotName);

        navigation.selectPortfolio(userMock);
        assertEquals(portfolio, navigation.selectedPortfolio);
    } */
}