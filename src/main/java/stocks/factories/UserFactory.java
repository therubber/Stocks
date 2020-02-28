package stocks.factories;

import stocks.entities.User;
import stocks.inputoutput.Input;
import stocks.inputoutput.Output;

public class UserFactory {

    private final Input input;
    private final Output output;

    public  UserFactory(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    public User createUser(String username) {
        return new User(username, input, output);
    }

    public User createUser(String username, String password) {
        return new User(username, password, input, output);
    }

}
