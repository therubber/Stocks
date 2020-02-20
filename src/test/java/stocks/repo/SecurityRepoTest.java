package stocks.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.Security;

import static org.junit.jupiter.api.Assertions.*;

class SecurityRepoTest {

    Security unirak = new Security("UniRAK");
    SecurityRepo securityRepo;

    @BeforeEach
    void setUp() {
        securityRepo.load();
    }

    @Test
    void get() {
        assertEquals(unirak, securityRepo.get(unirak));
    }

    @Test
    void testGet() {
        assertEquals(unirak, securityRepo.get("UniRAK"));
    }

    @Test
    void testGet1() {
        assertEquals(unirak, securityRepo.get(0));
    }

    @Test
    void contains() {
        assertTrue(securityRepo.contains(unirak));
        assertFalse(securityRepo.contains(new Security("Union")));
    }

    @Test
    void indexOf() {
        assertEquals(0, securityRepo.indexOf(unirak));
    }

    @Test
    void isEmpty() {
        assertFalse(securityRepo.isEmpty());
    }

    @Test
    void initiate() {
        assertFalse(securityRepo.isEmpty());
    }
}