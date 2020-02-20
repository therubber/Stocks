package stocks.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stocks.entities.User;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    static User user = new User("test");

    @BeforeAll
    static void setUp() {
        UserRepo.add(user);
    }

    @Test
    void get() {
        assertEquals(user, UserRepo.get("test"));
    }

    @Test
    void getAll() {
        assertTrue(UserRepo.contains(new User("test")));
    }

    @Test
    void contains() {
        assertTrue(UserRepo.contains(new User("test")));
    }

    @Test
    void testContains() {
        assertFalse(UserRepo.contains("asdfasdf"));
        assertTrue(UserRepo.contains(user));
    }

    @Test
    void indexOf() {
        UserRepo.add(new User("user"));
        assertEquals(1, UserRepo.indexOf(new User("user")));
    }

    @Test
    void isEmpty() {
        assertFalse(UserRepo.isEmpty());
    }
}