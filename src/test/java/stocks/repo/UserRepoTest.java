package stocks.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stocks.entities.User;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    static User user = new User("test");
    static UserRepo users = new UserRepo();

    @BeforeAll
    static void setUp() {
        users.add(user);
    }

    @Test
    void get() {
        assertEquals(user, users.get("test"));
    }

    @Test
    void getAll() {
        assertTrue(users.contains(new User("test")));
    }

    @Test
    void contains() {
        assertTrue(users.contains(new User("test")));
    }

    @Test
    void testContains() {
        assertFalse(users.contains(new User("asdfasdf")));
        assertTrue(users.contains(user));
    }

    @Test
    void indexOf() {
        users.add(new User("user"));
        assertEquals(1, users.indexOf(new User("user")));
    }

    @Test
    void isEmpty() {
        assertFalse(users.isEmpty());
    }
}