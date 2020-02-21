package stocks.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
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

    @BeforeEach
    void setUp() {
        securityRepo.load();
        users.load();
        securityDow = securityRepo.get("UniRAK");
        users.add(new User("testUser", "password"));
        portfolio = new Portfolio("test", "testUser", new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP), new Input());
        order = new Order(1, LocalDate.now(), "BUY", securityDow);
        portfolio.orderInput(order, users);
    }

    @Test
    void getName() {
        assertEquals("test", portfolio.getName());
    }

    @Test
    void getEquity() {
        assertEquals(new BigDecimal(Double.toString(4861.67)).setScale(2, RoundingMode.HALF_UP), portfolio.getEquity());
    }

    @Test
    void getStartequity() {
        assertEquals(new BigDecimal(Double.toString(5000)).setScale(2, RoundingMode.HALF_UP), portfolio.getStartEquity());
    }

    @Test
    void getValue() {
        assertEquals(new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP), portfolio.getValue());
    }

    @Test
    void getPosition() {
        assertEquals(new Position(order), portfolio.getPosition(0));
    }

    @Test
    void getPositionCount() {
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void getPositionValue() {
        assertEquals(new BigDecimal(Double.toString(138.33)).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
    }

    @Test
    void contains() {
        assertTrue(portfolio.contains("UniRAK"));
        assertFalse(portfolio.contains("UniAsia"));
    }

    @Test
    void orderInput() {
        portfolio.orderInput(order, users);
        assertEquals(new BigDecimal(Double.toString(276.66)).setScale(2, RoundingMode.HALF_UP), portfolio.getPositionValue());
        assertEquals(1, portfolio.getPositionCount());
        Position position = portfolio.getPosition(0);
        assertEquals(2, position.getCount());
        portfolio.orderInput(new Order(1, LocalDate.now(), "SELL", securityDow), users);
        assertEquals(1, portfolio.getPositionCount());
    }

    @Test
    void loadOwnedSecurities() {
        portfolio.loadOwnedSecurities();
        assertTrue(portfolio.contains("UniRAK"));
    }

    @Test
    void selectionBuy() {
        Input inputMock = Mockito.mock(Input.class);
        doReturn(4).when(inputMock).intValue();

        Security wienerSchnitzelAg = new Security("Wiener Schnitzel AG");
        SecurityRepo securityRepoMock = Mockito.mock(SecurityRepo.class);
        when(securityRepoMock.get(eq(3))).thenReturn(wienerSchnitzelAg);

        Portfolio portfolio = new Portfolio("schnitzel", "jay unit", LocalDate.of(2020, 2, 12), inputMock);
        Position resultPosition = portfolio.selectionBuy(securityRepoMock);

        assertEquals(4, resultPosition.getCount());
        assertSame(wienerSchnitzelAg, resultPosition.getSecurity());

        InOrder inOrder = Mockito.inOrder(securityRepoMock, inputMock);
        inOrder.verify(securityRepoMock).listIndexed();
        inOrder.verify(securityRepoMock).get(3);
    }
}