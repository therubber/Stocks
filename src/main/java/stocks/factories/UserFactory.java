package stocks.factories;

import stocks.entities.User;

public class UserFactory {

    public User createUser(String username) {
        return new User(username);
    }

    public User createUser(String username, String password) {
        return new User(username, password);
    }

}
