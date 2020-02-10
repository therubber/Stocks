package stocks.inputoutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stocks.dows.SecurityDow;
import stocks.dows.SpotPrice;
import stocks.entities.User;
import stocks.interfaces.Security;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class IO {

    public static class Load {

        static Navigation fromJson() {
            Navigation instance = new Navigation();
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
                Gson gson = new Gson();
                instance.setUsers(new LinkedList<>(new Gson().fromJson(jsonInput, new TypeToken<List<User>>(){}.getType())));
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
            return initiateSecurities(instance);
        }

        /**
         * Sets up names of available security and fetches data using updateSecurities()
         */
        private static Navigation initiateSecurities(Navigation instance) {
            try {
                Scanner input = new Scanner(new File("SecurityData/Securities.txt"));
                while (input.hasNext()) {
                    String name = input.next();
                    String isin = input.next();
                    String wkn = input.next();
                    if (!instance.getSecurities().contains(new SecurityDow(name, isin, wkn))) {
                        instance.getSecurities().add(new SecurityDow(name, isin, wkn));
                    }
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("Error: File Securities.txt not found. Unable to load securities.");
            }
            return updateSpotPrices(instance);
        }

        /**
         * Updates prices to most recent using data from SecurityData file.
         */
        private static Navigation updateSpotPrices(Navigation instance) {
            try {
                String pathname = "SecurityData/" + LocalDate.now().toString() + ".txt";
                Scanner input = new Scanner(new File(pathname));
                for (Security security : instance.getSecurities()) {
                    if (input.next().equals(security.getName())) {
                        if (input.next().equals(security.getIsin())) {
                            if (input.next().equals(security.getWkn())) {
                                security.setSpotPrice(new SpotPrice(input.nextDouble(), LocalDate.now().toString()));
                            } else {
                                System.out.println("WKN does not match!");
                            }
                        } else {
                            System.out.println("ISIN does not match!");
                        }
                    } else {
                        System.out.println("Datafile " + pathname + " does not match security " + security + "!");
                    }
                }
                input.close();
            } catch (FileNotFoundException fnfe) {
                System.out.println("No Update file found!");
                for (Security security : instance.getSecurities()) {
                    security.setSpotPrice(new SpotPrice(0, "UPDATE ERROR"));
                    System.out.println("UPDATE ERROR: " + security.getName());
                }
            }
            return instance;
        }
    }

    public static class Save {

        /**
         * Saves instance of Navigation to Saves.users,json
         * @param instance Instance of Navigation to be saved
         */
        static void toJson(Navigation instance) {
            Gson gson = new Gson();
            try {
                FileOutputStream fileOutputStream= new FileOutputStream("Saves/users.json");
                String save = gson.toJson(instance.getUsers());
                fileOutputStream.write(save.getBytes());
                fileOutputStream.close();
            }  catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static class Help {

        /**
         * Private Constructor to hide public one
         */
        private Help() {
        }

        /**
         * Displays menu when no user is logged in
         */
        static void noUser() {
            System.out.printf("%n%-15s %s%n", "login:", "Log in with existing user");
            System.out.printf("%-15s %s%n", "add:", "Register a new user");
            System.out.printf("%-15s %s%n", "lf", "Displays a list of all securities available in Stocks");
            System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
            System.out.printf("%-15s %s%n", "clear:", "Clears the console");
            System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
        }

        /**
         * Displays menu when a user is logged in
         */
        static void loggedIn() {
            System.out.printf("%n%-15s %s%n", "selected:", "Overview of user & portfolio currently selected");
            System.out.printf("%-15s %s%n", "select:", "Select an existing portfolio");
            System.out.printf("%-15s %s%n","add:", "Add a new portfolio");
            System.out.printf("%-15s %s%n", "lp:", "List all portfolios");
            System.out.printf("%-15s %s%n", "lf:", "List all securities");
            System.out.printf("%-15s %s%n", "ph:", "Select a security and show its price history");
            System.out.printf("%-15s %s%n", "buy:", "Add a new position to the selected portfolio");
            System.out.printf("%-15s %s%n", "sell:", "Reduce an existing position in the selected portfolio");
            System.out.printf("%-15s %s%n", "ov:", "Overview of all positions in selected portfolio");
            System.out.printf("%-15s %s%n", "oh:", "Displays order history");
            System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
            System.out.printf("%-15s %s%n", "clear:", "Clears the console");
            System.out.printf("%-15s %s%n", "logout:", "Logs current user out and displays the login menu");
            System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
        }

        /**
         * Clears console window
         */
        static void clear() {
            System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
        }
    }
}
