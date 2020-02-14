package stocks.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stocks.entities.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    static User user = new User("test");

    @BeforeAll
    static void setUp() {
        Users.add(user);
    }

    @Test
    void get() {
        assertEquals(user, Users.get("test"));
    }

    @Test
    void getAll() {
        assertTrue(Users.contains(new User("test")));
    }

    @Test
    void contains() {
        assertTrue(Users.contains(new User("test")));
    }

    @Test
    void testContains() {
        assertFalse(Users.contains("asdfasdf"));
        assertTrue(Users.contains(user));
    }

    @Test
    void indexOf() {
        Users.add(new User("user"));
        assertEquals(1, Users.indexOf(new User("user")));
    }

    @Test
    void isEmpty() {
        assertFalse(Users.isEmpty());
    }
}