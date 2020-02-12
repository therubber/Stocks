package stocks.inputoutput;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stocks.dows.SecurityDow;
import stocks.dows.SpotPrice;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import java.io.*;
import java.security.Security;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class IO {

    /** Private constructor to hide public one */
    private IO() {}

    public static class Load {

        /** Private constructor to hide public one */
        private Load() {}

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
                instance.setUsers(new LinkedList<>(new Gson().fromJson(jsonInput, new TypeToken<List<User>>(){}.getType())));
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
            return initiateSecurities(instance);
        }

        /**
         * Sets up names of available security and fetches data using updateSecurities()
         */
        private static Navigation initiateSecurities(Navigation instance) {
            try {
                BufferedReader input = new BufferedReader(new FileReader("SecurityData/Securities.csv"));
                String line;
                while ((line = input.readLine()) != null) {
                    String[] fund = line.split(";");
                    String name = fund[0];
                    String isin = fund[1];
                    String wkn = fund[2];
                    if (!instance.getSecurities().contains(new SecurityDow(name, isin, wkn))) {
                        instance.getSecurities().add(new SecurityDow(name, isin, wkn));
                    }
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("Error: File Securities.csv not found. Unable to load securities.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return updateSpotPrice(instance);
        }

        /**
         * Updates prices to most recent using data from SecurityData file and fills price history with all other found historical prices
         */
        private static Navigation updateSpotPrice(Navigation instance) {
            try {
                String pathname = "SecurityData/SpotData/";
                File spotDir = new File(pathname);
                File[] spotFiles = spotDir.listFiles();
                assert spotFiles != null;
                for (File file : spotFiles) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    for (SecurityDow security : instance.getSecurities()) {
                        String line = bufferedReader.readLine();
                        String[] data = line.split(";");
                        if (data[0].equals(security.getName()) && data[1].equals(security.getIsin()) && data[2].equals(security.getWkn())) {
                            security.setSpotPrice(new SpotPrice(Double.parseDouble(data[3]), LocalDate.parse(file.getName().replace(".csv", ""))));
                        } else {
                            System.out.println("Datafile " + pathname + " does not match security " + security + "!");
                        }
                    }
                    bufferedReader.close();
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("No Update file found!");
                for (SecurityDow security : instance.getSecurities()) {
                    System.out.println("UPDATE ERROR: " + security.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return updatePortfolio(instance);
        }

        private static Navigation updatePortfolio(Navigation instance) {
            for (User user : instance.getUsers()) {
                for (Portfolio portfolio : user.getPortfolios()) {
                    portfolio.setOwnedSecurities();
                    for (Position position : portfolio.getPositions()) {
                        SecurityDow positionSecurity = position.getSecurity();
                        if (instance.getSecurities().contains(positionSecurity)) {
                            SecurityDow instanceSecurity = instance.getSecurities().get(instance.getSecurities().indexOf(positionSecurity));
                            SpotPrice positionSpotPrice = positionSecurity.getSpotPrice();
                            SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                            if (!positionSpotPrice.equals(instanceSpotPrice)) {
                                positionSecurity.setSpotPrice(instanceSpotPrice);
                            }
                        }
                    }
                    for (SecurityDow security : portfolio.ownedSecurities) {
                        if (instance.getSecurities().contains(security)) {
                            SecurityDow instanceSecurity = instance.getSecurities().get(instance.getSecurities().indexOf(security));
                            SpotPrice positionSpotPrice = security.getSpotPrice();
                            SpotPrice instanceSpotPrice = instanceSecurity.getSpotPrice();
                            if (!positionSpotPrice.equals(instanceSpotPrice)) {
                                security.setSpotPrice(instanceSpotPrice);
                            }
                        }
                    }
                }
            }
            return instance;
        }
    }

    public static class Save {

        /** Private constructor to hide public one */
        private Save() {}

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

        static final String FORMAT = "%-15s %s%n";

        /** Private Constructor to hide public one */
        private Help() {}

        /**
         * Displays menu when no user is logged in
         */
        static void noUser() {
            System.out.println();
            System.out.printf(FORMAT, "login:", "Log in with existing user");
            System.out.printf(FORMAT, "add:", "Register a new user");
            System.out.printf(FORMAT, "lf", "Displays a list of all securities available in Stocks");
            System.out.printf(FORMAT, "help:", "Shows this dialog");
            System.out.printf(FORMAT, "clear:", "Clears the console");
            System.out.printf(FORMAT, "exit:", "Exit Stocks");
            System.out.println();
        }

        /**
         * Displays menu when a user is logged in
         */
        static void loggedIn() {
            System.out.println();
            System.out.printf(FORMAT, "selected:", "Overview of user & portfolio currently selected");
            System.out.printf(FORMAT, "select:", "Select an existing portfolio");
            System.out.printf(FORMAT,"add:", "Add a new portfolio");
            System.out.printf(FORMAT, "lp:", "List all portfolios");
            System.out.printf(FORMAT, "lf:", "List all securities");
            System.out.printf(FORMAT, "ph:", "Select a security and show its price history");
            System.out.printf(FORMAT, "buy:", "Add a new position to the selected portfolio");
            System.out.printf(FORMAT, "sell:", "Reduce an existing position in the selected portfolio");
            System.out.printf(FORMAT, "ov:", "Overview of all positions in selected portfolio");
            System.out.printf(FORMAT, "oh:", "Displays order history");
            System.out.printf(FORMAT, "help:", "Shows this dialog");
            System.out.printf(FORMAT, "clear:", "Clears the console");
            System.out.printf(FORMAT, "logout:", "Logs current user out and displays the login menu");
            System.out.printf(FORMAT, "exit:", "Exit Stocks");
            System.out.println();
        }

        /**
         * Clears console window
         */
        static void clear() {
            System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
        }
    }
}
