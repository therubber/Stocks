package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import stocks.utility.Factory;
import stocks.inputoutput.Input;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PortfolioTest {

    Portfolio portfolio;
    Security securityDow;
    Order order;
    SecurityRepo securityRepo = new SecurityRepo();
    UserRepo users = new UserRepo();
    private final Factory factory = new Factory();
    Portfolio portfolioMock = Mockito.mock(Portfolio.class);

    @BeforeEach
    void setUp() {
        securityRepo.load();
        users.load();
        securityDow = securityRepo.get("UniRAK");
        users.add(factory.createUser("testUser", "password"));
        portfolio = factory.createPortfolio("test", "testUser", factory.createBigDecimal(5000), new Input());
        order = factory.createOrder(5, LocalDate.now(), "BUY", securityDow);
        portfolio.orderInput(order, users);
    }

    @Test
    void getName() {
        assertEquals("test", portfolio.getName());
    }

    @Test
    void getEquity() {
        assertEquals(factory.createBigDecimal(4308.35), portfolio.getEquity());
    }

    @Test
    void getStartequity() {
        assertEquals(factory.createBigDecimal(5000), portfolio.getStartEquity());
    }

    @Test
    void getValue() {
        assertEquals(factory.createBigDecimal(5000.00), portfolio.getValue());
    }

    @Test
    void getPosition() {
        assertEquals(factory.createPosition(order), portfolio.getPosition(0));
    }

    @Test
    void getPositionCount() {
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void getPositionValue() {
        assertEquals(factory.createBigDecimal(691.65), portfolio.getPositionValue());
    }

    @Test
    void orderInput() {
        portfolio.orderInput(order, users);
        assertEquals(factory.createBigDecimal(1383.30), portfolio.getPositionValue());
        assertEquals(1, portfolio.getPositionCount());
        Position position = portfolio.getPosition(0);
        assertEquals(10, position.getCount());
        portfolio.orderInput(factory.createOrder(1, LocalDate.now(), "SELL", securityDow), users);
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void selectionBuy() {
        Input inputMock = Mockito.mock(Input.class);
        doReturn(4).when(inputMock).intValue();

        Security wienerSchnitzelAg = new Security("Wiener Schnitzel AG");
        SecurityRepo securityRepoMock = Mockito.mock(SecurityRepo.class);
        when(securityRepoMock.get(eq(3))).thenReturn(wienerSchnitzelAg);

        Portfolio portfolio = factory.createPortfolio("schnitzel", "jay unit", LocalDate.of(2020, 2, 12), inputMock);
        Position resultPosition = portfolio.selectionBuy(securityRepoMock);

        assertEquals(4, resultPosition.getCount());
        assertSame(wienerSchnitzelAg, resultPosition.getSecurity());
        InOrder inOrder = Mockito.inOrder(securityRepoMock, inputMock);
        inOrder.verify(securityRepoMock).listIndexed();
        inOrder.verify(securityRepoMock).get(3);
    }

    @Test
    void overview() {
        doReturn(factory.createBigDecimal(0)).when(portfolioMock).getPositionValue();
        doReturn(factory.createBigDecimal(500)).when(portfolioMock).getEquity();
        doReturn(factory.createBigDecimal(500)).when(portfolioMock).getValue();
        portfolio.overview(portfolioMock);

    }
}