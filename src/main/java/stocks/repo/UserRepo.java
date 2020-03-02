package stocks.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stocks.entities.User;
import stocks.factories.UserFactory;
import stocks.inputoutput.Input;
import stocks.inputoutput.Output;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class UserRepo {

    private List<User> users = new LinkedList<>();
    private final Input input = new Input();
    private final Output output = new Output();
    private final UserFactory factory = new UserFactory(input, output);

    /**
     * Getter method to get a User out of the list
     * @param name String name of the user
     * @return User user
     */
    public User get(String name) {
        return users.get(users.indexOf(factory.createUser(name)));
    }

    /**
     * Asks for input to create a new User and creates it
     */
    public User addUser() {
        output.println("Enter a username to create a new user: ");
        String username = input.stringValue();
        if (!contains(factory.createUser(username))) {
            output.println("Please enter a password: ");
            User user = factory.createUser(username, input.stringValue());
            users.add(user);
            output.println("New user " + username + " has been created!");
            // save();
            return user;
        } else {
            output.println("User with that username already exists, please login.");
            return null;
        }
    }

    /**
     * Checks whether the user base contains a certain user
     * @param user User to check containment
     * @return Boolean whether the user is contained in the list
     */
    public boolean contains(User user) {
        return users.contains(user);
    }

    /**
     * Checks whether the user base contains any users
     * @return Boolean whether the list of users is empty
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }

    /**
     * Saves the list of all users to /Saves/users.json file
     */
    public void save() {
        try {
            FileOutputStream fileOutputStream= new FileOutputStream("Saves/users.json");
            String json = new Gson(). toJson(users);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        }  catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Loads all users into Users class from /Saves/users.json
     */
    public void load() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("Saves/users.json")));
            StringBuilder sb = new StringBuilder();
            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                sb.append(inputString);
            }
            String jsonInput = sb.toString();
            users = new LinkedList<>(new Gson().fromJson(jsonInput, new TypeToken<List<User>>(){}.getType()));
            bufferedReader.close();
        } catch (FileNotFoundException fnfe) {
            output.println("No Savefile found, creating new one...");
            try {
                File saveDir = new File("Saves");
                if (!saveDir.exists()) {
                    if (saveDir.mkdir()) {
                        output.println("/Saves/ Directory created!");
                    }
                } else {
                    File save = new File(saveDir.getPath() + "users.json");
                    if (save.createNewFile()) {
                        output.println("New save created!");
                    }
                }
            } catch (IOException ioe) {
                output.println("New Save could not be created. Progress will not be saved.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            output.println("Savefile Corrupted! Creating new Save...");
        }
    }
}
