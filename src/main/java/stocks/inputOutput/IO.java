package stocks.inputOutput;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import stocks.dows.SecurityDow;
import stocks.dows.SpotPrice;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.interfaces.Security;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class IO {

    public static class Load {

        public static Navigation fromXml() {
            Navigation instance = new Navigation();
            try {
                XMLDecoder decoder = new XMLDecoder(new ObjectInputStream(new FileInputStream("Saves/recent.xml")));
                instance.setUsers((List<User>) decoder.readObject());
            } catch (IOException ioe) {
                System.out.println("No recent save found. Creating new one...");
                try {
                    File save = new File("Saves/recent.xml");
                    if (!save.createNewFile()) {
                        System.out.println("New save created!");
                    }
                } catch (IOException fileioe) {
                    System.out.println("New Save could not be created. Progress will not be saved.");
                }
            }
            instance = initiateSecurities(instance);
            return instance;
        }

        /**
         * Sets up names of available security and fetches data using updateSecurities()
         */
        public static Navigation initiateSecurities(Navigation instance) {
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
            instance = updateSpotPrices(instance);
            return instance;
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
                }
            }
            return instance;
        }

        public static Navigation fromJson() {
            Navigation instance = new Navigation();
            // TODO: Load instance from JSON file
            return instance;
        }
    }

    public static class Save {

        public static void toJson(Navigation currentInstance) {
            JSONArray userListJson = new JSONArray();
            for (User user : currentInstance.getUsers()) {
                JSONObject userJson = new JSONObject();
                JSONArray userPortfoliosJson = new JSONArray();
                userJson.put("username", user.getUsername());
                userJson.put("equity", user.getEquity());
                List<Portfolio> userPortfolios = user.getPortfolios();
                for (Portfolio portfolio : userPortfolios) {
                    JSONObject portfolioJson = new JSONObject();
                    portfolioJson.put("name", portfolio.getName());
                    portfolioJson.put("equity", portfolio.getEquity());
                    portfolioJson.put("owner", user);
                    JSONArray positionsJson = new JSONArray();
                    for (Position position : portfolio.getPositions()) {
                        JSONObject positionJson = new JSONObject();
                        JSONObject securityJson = new JSONObject();
                        JSONArray historicalPricesJson = new JSONArray();
                        securityJson.put("name", position.getSecurity().getName());
                        securityJson.put("isin", position.getSecurity().getIsin());
                        securityJson.put("wkn", position.getSecurity().getWkn());
                        for (SpotPrice price : position.getSecurity().getHistoricalPrices()) {
                            JSONObject histPriceJson = new JSONObject();
                            histPriceJson.put("price", price.getPrice());
                            histPriceJson.put("date", price.getDate());
                            historicalPricesJson.add(histPriceJson);
                        }
                        securityJson.put("historicalPrices", historicalPricesJson);
                        positionJson.put("count", position.getCount());
                        positionJson.put("id", position.getId());
                        positionJson.put("executionDate", position.getExecutionDate());
                        positionJson.put("security", position.getSecurity());
                        positionsJson.add(positionJson);
                    }
                    portfolioJson.put("positions", positionsJson);
                    userPortfoliosJson.add(portfolioJson);
                }
                userJson.put("portfolios", userPortfoliosJson);
                userListJson.add(userJson);
            }
            try (FileWriter file = new FileWriter("users.json")) {
                file.write(userListJson.toJSONString());
                file.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public static void toXml(Navigation currentInstance) {
            try {
                final XMLEncoder encoder = new XMLEncoder(new ObjectOutputStream(new FileOutputStream("Saves/recent.xml")));
                encoder.writeObject(currentInstance.getUsers());
                encoder.close();
            } catch (IOException ioe) {
                System.out.println("Savefile not found, saving failed!");
            }
        }
    }

    public static class Help {

        private Help() {
        }

        static void noUser() {
            System.out.printf("%n%-15s %s%n", "login:", "Log in with existing user");
            System.out.printf("%-15s %s%n", "add:", "Register a new user");
            System.out.printf("%-15s %s%n", "lf", "Displays a list of all securities available in Stocks");
            System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
            System.out.printf("%-15s %s%n", "clear:", "Clears the console");
            System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
        }

        static void loggedIn() {
            System.out.printf("%n%-15s %s%n", "selected:", "Overview of user & portfolio currently selected");
            System.out.printf("%-15s %s%n", "select:", "Select an existing portfolio");
            System.out.printf("%-15s %s%n","add:", "Add a new portfolio");
            System.out.printf("%-15s %s%n", "lp:", "List all portfolios");
            System.out.printf("%-15s %s%n", "lf:", "List all securities");
            System.out.printf("%-15s %s%n", "buy:", "Add a new position to the selected portfolio");
            System.out.printf("%-15s %s%n", "sell:", "Reduce an existing position in the selected portfolio");
            System.out.printf("%-15s %s%n", "ov:", "Overview of all positions in selected portfolio");
            System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
            System.out.printf("%-15s %s%n", "clear:", "Clears the console");
            System.out.printf("%-15s %s%n", "logout:", "Logs current user out and displays the login menu");
            System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
        }

        static void clear() {
            System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
        }
    }
}
