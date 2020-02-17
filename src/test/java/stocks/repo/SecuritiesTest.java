package stocks.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.Security;

import static org.junit.jupiter.api.Assertions.*;

class SecuritiesTest {

    Security unirak = new Security("UniRAK");

    @BeforeEach
    void setUp() {
        Securities.load();
    }

    @Test
    void get() {
        assertEquals(unirak, Securities.get(unirak));
    }

    @Test
    void testGet() {
        assertEquals(unirak, Securities.get("UniRAK"));
    }

    @Test
    void testGet1() {
        assertEquals(unirak, Securities.get(0));
    }

    @Test
    void contains() {
        assertTrue(Securities.contains(unirak));
        assertFalse(Securities.contains(new Security("Union")));
    }

    @Test
    void indexOf() {
        assertEquals(0, Securities.indexOf(unirak));
    }

    @Test
    void isEmpty() {
        assertFalse(Securities.isEmpty());
    }

    @Test
    void initiate() {
        assertFalse(Securities.isEmpty());
    }
}