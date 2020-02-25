package stocks.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stocks.entities.User;
import stocks.factories.UserFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    private final static UserFactory userFactory = new UserFactory();
    private static User user = userFactory.createUser("test");
    private static UserRepo users = new UserRepo();

    @BeforeAll
    static void setUp() {
        users.addUser(user);
    }

    @Test
    void get() {
        assertEquals(user, users.get("test"));
    }

    @Test
    void getAll() {
        assertTrue(users.contains(userFactory.createUser("test")));
    }

    @Test
    void contains() {
        assertTrue(users.contains(userFactory.createUser("test")));
    }

    @Test
    void testContains() {
        assertFalse(users.contains(userFactory.createUser("asdfasdf")));
        assertTrue(users.contains(user));
    }

    @Test
    void indexOf() {
        users.addUser(userFactory.createUser("user"));
        assertEquals(1, users.indexOf(userFactory.createUser("user")));
    }

    @Test
    void isEmpty() {
        assertFalse(users.isEmpty());
    }
}