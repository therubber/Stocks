package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import stocks.factories.NumberFactory;
import stocks.factories.PortfolioFactory;
import stocks.factories.SecurityFactory;
import stocks.factories.UserFactory;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PortfolioSnapshotTest {

    PortfolioSnapshot portfolioSnapshot;
    Security securityDow;
    Order order;
    SecurityRepo securityRepo = new SecurityRepo();
    UserRepo users = new UserRepo();
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();
    private final UserFactory userFactory = new UserFactory();
    PortfolioSnapshot portfolioSnapshotMock = Mockito.mock(PortfolioSnapshot.class);
    List<Position> positions = new LinkedList<>();

    @BeforeEach
    void setUp() {
        securityRepo.load();
        users.load();
        securityDow = securityRepo.get("UniRAK");
        users.addUser(userFactory.createUser("testUser", "password"));
        portfolioSnapshot = portfolioFactory.createPortfolioSnapshot("test", "testUser", numberFactory.createBigDecimal(5000), new Input());
        order = portfolioFactory.createOrder(5, "BUY", securityDow);
        portfolioSnapshot.orderInput(order, users);

        SpotPrice spotPrice = new SpotPrice(5.0, LocalDate.now());
        Security security = securityFactory.createSecurity("coole firma", "12345678", "12345678", "Schnitzel");
        Position position = new Position(5, security);
        positions.add(position);
    }

    @Test
    void getName() {
        assertEquals("test", portfolioSnapshot.getName());
    }

    @Test
    void getEquity() {
        assertEquals(numberFactory.createBigDecimal(4308.35), portfolioSnapshot.getEquity());
    }

    @Test
    void getStartequity() {
        assertEquals(numberFactory.createBigDecimal(5000), portfolioSnapshot.getStartEquity());
    }

    @Test
    void getValue() {
        assertEquals(numberFactory.createBigDecimal(5000.00), portfolioSnapshot.getValue());
    }

    @Test
    void getPosition() {
        assertEquals(portfolioFactory.createPosition(order), portfolioSnapshot.getPosition(0));
    }

    @Test
    void getPositionCount() {
        assertEquals(1, portfolioSnapshot.getPositionCount());
    }

    @Test
    void getPositionValue() {
        PortfolioSnapshot portfolioSnapshotMock = Mockito.mock(PortfolioSnapshot.class);
        Position positionMock = Mockito.mock(Position.class);
        portfolioSnapshotMock.addPosition(positionMock);
        doReturn(numberFactory.createBigDecimal(5)).when(positionMock).getValue();
        assertEquals(numberFactory.createBigDecimal(5), portfolioSnapshotMock.getPositionValue(positions));
    }

    @Test
    void orderInput() {
        portfolioSnapshot.orderInput(order, users);
        assertEquals(numberFactory.createBigDecimal(1383.30), portfolioSnapshot.getPositionValue(positions));
        assertEquals(1, portfolioSnapshot.getPositionCount());
        Position position = portfolioSnapshot.getPosition(0);
        assertEquals(10, position.getCount());
        portfolioSnapshot.orderInput(portfolioFactory.createOrder(1, "SELL", securityDow), users);
        assertEquals(1, portfolioSnapshot.getPositionCount());
    }

    @Test
    void selectionBuy() {
        Input inputMock = Mockito.mock(Input.class);
        doReturn(4).when(inputMock).intValue();

        Security wienerSchnitzelAg = new Security("Wiener Schnitzel AG");
        SecurityRepo securityRepoMock = Mockito.mock(SecurityRepo.class);
        when(securityRepoMock.get(eq(3))).thenReturn(wienerSchnitzelAg);

        PortfolioSnapshot portfolioSnapshot = portfolioFactory.createPortfolioSnapshot("schnitzel", "jay unit", LocalDate.of(2020, 2, 12), inputMock);
        Position resultPosition = portfolioSnapshot.selectionBuy(securityRepoMock);

        assertEquals(4, resultPosition.getCount());
        assertSame(wienerSchnitzelAg, resultPosition.getSecurity());
        InOrder inOrder = Mockito.inOrder(securityRepoMock, inputMock);
        inOrder.verify(securityRepoMock).listIndexed();
        inOrder.verify(securityRepoMock).get(3);
    }

    @Test
    void overview() {
        doReturn(numberFactory.createBigDecimal(0)).when(portfolioSnapshotMock).getPositionValue(positions);
        doReturn(numberFactory.createBigDecimal(500)).when(portfolioSnapshotMock).getEquity();
        doReturn(numberFactory.createBigDecimal(500)).when(portfolioSnapshotMock).getValue();
        portfolioSnapshot.overview(portfolioSnapshotMock);
    }
}