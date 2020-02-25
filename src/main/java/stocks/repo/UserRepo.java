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
    private final UserFactory factory = new UserFactory();
    private final Input input = new Input();
    private final Output out = new Output();

    /**
     * Getter method to get a User out of the list
     * @param name String name of the user
     * @return User user
     */
    public User get(String name) {
        return users.get(users.indexOf(factory.createUser(name)));
    }

    /**
     * Adds a user
     * @param user User to be added
     */
    public void addUser(User user) {
        users.add(user);
    }

    public void addUser() {
        out.println("Enter a username to create a new user: ");
        String username = input.stringValue();
        if (!contains(factory.createUser(username))) {
            out.println("Please enter a password: ");
            User user = factory.createUser(username, input.stringValue());
            users.add(user);
            out.println("New user " + username + " has been created!");
        } else {
            out.println("User with that username already exists, please login.");
        }
        save();
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
     * Gets the index of a user if he is contained in the list. Returns -1 if not.
     * @param user User to check index
     * @return Int index of user in the list, -1 if not contained
     */
    public int indexOf(User user) {
        return users.indexOf(user);
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
            String json = new Gson().toJson(users);
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
            System.out.println("No Savefile found, creating new one...");
            try {
                File saveDir = new File("Saves");
                if (!saveDir.exists()) {
                    if (saveDir.mkdir()) {
                        System.out.println("/Saves/ Directory created!");
                    }
                } else {
                    File save = new File(saveDir.getPath() + "users.json");
                    if (save.createNewFile()) {
                        System.out.println("New save created!");
                    }
                }
            } catch (IOException ioe) {
                System.out.println("New Save could not be created. Progress will not be saved.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Savefile Corrupted! Creating new Save...");
        }
    }
}
