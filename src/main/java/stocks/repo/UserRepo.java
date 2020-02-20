package stocks.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stocks.entities.User;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class UserRepo {

    private UserRepo() {}

    static List<User> userList = new LinkedList<>();

    /**
     * Getter method to get a User out of the list
     * @param name String name of the user
     * @return User user
     */
    public static User get(String name) {
        return userList.get(userList.indexOf(new User(name)));
    }

    /**
     * Adds a user
     * @param user User to be added
     */
    public static void add(User user) {
        userList.add(user);
    }

    /**
     * Checks whether the user base contains a certain user
     * @param user User to check containment
     * @return Boolean whether the user is contained in the list
     */
    public static boolean contains(User user) {
        return userList.contains(user);
    }

    public static boolean contains(String name) {
        return userList.contains(new User(name));
    }

    /**
     * Gets the index of a user if he is contained in the list. Returns -1 if not.
     * @param user User to check index
     * @return Int index of user in the list, -1 if not contained
     */
    public static int indexOf(User user) {
        return userList.indexOf(user);
    }

    /**
     * Checks whether the user base contains any users
     * @return Boolean whether the list of users is empty
     */
    public static boolean isEmpty() {
        return userList.isEmpty();
    }

    /**
     * Saves the list of all users to /Saves/users.json file
     */
    public static void save() {
        Gson gson = new Gson();
        try {
            FileOutputStream fileOutputStream= new FileOutputStream("Saves/users.json");
            String save = gson.toJson(userList);
            fileOutputStream.write(save.getBytes());
            fileOutputStream.close();
        }  catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Loads all users into Users class from /Saves/users.json
     */
    public static void load() {
        try {
            FileInputStream fileInputStream = new FileInputStream("Saves/users.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                sb.append(input);
            }
            String jsonInput = sb.toString();
            userList = new LinkedList<>(new Gson().fromJson(jsonInput, new TypeToken<List<User>>(){}.getType()));
            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();
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
        }
    }
}
