package stocks.repo;

import org.junit.jupiter.api.Test;
import stocks.entities.User;
import stocks.factories.UserFactory;
import stocks.inputoutput.Input;
import stocks.inputoutput.Output;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    private final static UserFactory userFactory = new UserFactory(new Input(), new Output());
    private static User user = userFactory.createUser("test");
    private static UserRepo users = new UserRepo();

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
    void isEmpty() {
        assertFalse(users.isEmpty());
    }
}