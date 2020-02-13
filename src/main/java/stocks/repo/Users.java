package stocks.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stocks.entities.Portfolio;
import stocks.entities.User;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Users {

    private Users() {}

    public static List<User> userList = new LinkedList<>();

    public static User get(String name) {
        return userList.get(userList.indexOf(new User(name)));
    }

    public static List<User> getAll() {
        return userList;
    }

    public static void add(User user) {
        userList.add(user);
    }

    public static boolean contains(User user) {
        return userList.contains(user);
    }

    public static boolean contains(String name) {
        return userList.contains(new User(name));
    }

    public static int indexOf(User user) {
        return userList.indexOf(user);
    }

    public static boolean isEmpty() {
        return userList.isEmpty();
    }

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

    public static void updatePortfolios() {
        for (User user : userList) {
            for (Portfolio portfolio : user.getPortfolios()) {
                portfolio.update();
            }
        }
    }
}
