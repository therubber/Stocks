package stocks.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @BeforeAll
    static void setUp() {
        Users.add(new User("user"));
    }

    @Test
    void get() {
        assertEquals(new User("user"), Users.get("user"));
    }

    @Test
    void getAll() {
        List<User> users = Users.getAll();
        assertTrue(users.contains(new User("user")));
    }

    @Test
    void contains() {
        assertTrue(Users.contains(new User("user")));
    }

    @Test
    void testContains() {
        assertTrue(Users.contains(new User("test")));
    }

    @Test
    void indexOf() {
        assertEquals(0, Users.indexOf(new User("user")));
    }

    @Test
    void isEmpty() {
        assertFalse(Users.isEmpty());
    }
}