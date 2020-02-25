package stocks.entities;

import org.junit.jupiter.api.Assertions;
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

class PortfolioTest {

    Portfolio portfolio;
    Security securityDow;
    Order order;
    SecurityRepo securityRepo = new SecurityRepo();
    UserRepo users = new UserRepo();
    private final NumberFactory numberFactory = new NumberFactory();
    private final PortfolioFactory portfolioFactory = new PortfolioFactory();
    private final SecurityFactory securityFactory = new SecurityFactory();
    private final UserFactory userFactory = new UserFactory();
    Portfolio portfolioMock = Mockito.mock(Portfolio.class);
    List<Position> positions = new LinkedList<>();

    @BeforeEach
    void setUp() {
        securityRepo.load();
        users.load();
        securityDow = securityRepo.get("UniRAK");
        users.addUser(userFactory.createUser("testUser", "password"));
        portfolio = portfolioFactory.createPortfolio("test", "testUser", numberFactory.createBigDecimal(5000), new Input());
        order = portfolioFactory.createOrder(5, "BUY", securityDow);
        portfolio.orderInput(order, users);

        SpotPrice spotPrice = new SpotPrice(5.0, LocalDate.now());
        Security security = securityFactory.createSecurity("coole firma", "12345678", "12345678", "Schnitzel");
        Position position = new Position(5, security);
        positions.add(position);
    }

    @Test
    void getName() {
        Assertions.assertEquals("test", portfolio.getName());
    }

    @Test
    void getEquity() {
        Assertions.assertEquals(numberFactory.createBigDecimal(4308.35), portfolio.getEquity());
    }

    @Test
    void getStartequity() {
        Assertions.assertEquals(numberFactory.createBigDecimal(5000), portfolio.getStartEquity());
    }

    @Test
    void getValue() {
        Assertions.assertEquals(numberFactory.createBigDecimal(5000.00), portfolio.getValue());
    }

    @Test
    void getPosition() {
        Assertions.assertEquals(portfolioFactory.createPosition(order), portfolio.getPosition(0));
    }

    @Test
    void getPositionCount() {
        Assertions.assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void getPositionValue() {
        Portfolio portfolioMock = Mockito.mock(Portfolio.class);
        Position positionMock = Mockito.mock(Position.class);
        portfolioMock.addPosition(positionMock);
        doReturn(numberFactory.createBigDecimal(5)).when(positionMock).getValue();
        Assertions.assertEquals(numberFactory.createBigDecimal(5), portfolioMock.getPositionValue());
    }

    @Test
    void orderInput() {
        portfolio.orderInput(order, users);
        Assertions.assertEquals(numberFactory.createBigDecimal(1383.30), portfolio.getPositionValue());
        Assertions.assertEquals(1, portfolio.getPositionCount());
        Position position = portfolio.getPosition(0);
        assertEquals(10, position.getCount());
        portfolio.orderInput(portfolioFactory.createOrder(1, "SELL", securityDow), users);
        Assertions.assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void selectionBuy() {
        Input inputMock = Mockito.mock(Input.class);
        doReturn(4).when(inputMock).intValue();

        Security wienerSchnitzelAg = new Security("Wiener Schnitzel AG");
        SecurityRepo securityRepoMock = Mockito.mock(SecurityRepo.class);
        when(securityRepoMock.get(eq(3))).thenReturn(wienerSchnitzelAg);

        Portfolio portfolio = portfolioFactory.createPortfolio("schnitzel", "jay unit", LocalDate.of(2020, 2, 12), inputMock);
        Position resultPosition = portfolio.selectionBuy(securityRepoMock);

        assertEquals(4, resultPosition.getCount());
        assertSame(wienerSchnitzelAg, resultPosition.getSecurity());
        InOrder inOrder = Mockito.inOrder(securityRepoMock, inputMock);
        inOrder.verify(securityRepoMock).listIndexed();
        inOrder.verify(securityRepoMock).get(3);
    }

    @Test
    void overview() {
        doReturn(numberFactory.createBigDecimal(0)).when(portfolioMock).getPositionValue();
        doReturn(numberFactory.createBigDecimal(500)).when(portfolioMock).getEquity();
        doReturn(numberFactory.createBigDecimal(500)).when(portfolioMock).getValue();
        portfolio.overview(portfolioMock);
    }
}