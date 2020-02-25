package stocks.inputoutput;

import stocks.entities.PortfolioSnapshot;
import stocks.entities.User;
import stocks.factories.UserFactory;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;

public class Navigation {

    private transient User selectedUser;
    private transient PortfolioSnapshot selectedPortfolioSnapshot;
    private static transient SecurityRepo availableSecurities = new SecurityRepo();
    private static UserRepo users = new UserRepo();
    private final Input input = new Input();
    private final Output out = new Output();
    private final UserFactory userFactory = new UserFactory();

    public static void main(String[] args) {
        Navigation instance = new Navigation();
        availableSecurities.load();
        users.load();
        instance.navigation();
    }

    private void navigation() {
        boolean exit = false;
        while (!exit) {
            if (selectedUser == null) {
                Help.noUser();
                exit = noUserNavigation();
            } else {
                if (selectedPortfolioSnapshot == null) {
                    Help.loggedIn();
                    exit = userSelected();
                } else {
                    Help.portfolio();
                    exit = portfolioSelected();
                }
            }
        }
    }

    private boolean noUserNavigation() {
        if (selectedUser == null) {
            switch (input.stringValue()) {
                case "login":
                    out.print("Please enter your username: ");
                    String username = input.stringValue();
                    login(userFactory.createUser(username));
                    return false;
                case "add":
                    users.addUser();
                    return false;
                case "lf":
                    availableSecurities.list();
                    return false;
                case "ph":
                    availableSecurities.priceHistory();
                    return false;
                case "help":
                    return false;
                case "clear":
                    Help.clear();
                    return false;
                case "exit":
                    users.save();
                    return true;
                default:
                    out.println("Invalid command, please try again.");
                    return false;
            }
        }
        return false;
    }

    private boolean userSelected() {
        if (selectedPortfolioSnapshot != null) {
            return portfolioSelected();
        } else {
            switch (input.stringValue()) {
                case "selected":
                    selected();
                    return false;
                case "select":
                    selectPortfolio();
                    return false;
                case "add":
                    selectedUser.addPortfolio();
                    users.save();
                    return false;
                case "lp":
                    selectedUser.listPortfolios();
                    return false;
                case "lf":
                    availableSecurities.list();
                    return false;
                case "ph":
                    availableSecurities.priceHistory();
                    return false;
                case "oh":
                    selectedUser.printOrderHistory();
                    return false;
                case "help":
                    return false;
                case "clear":
                    Help.clear();
                    return false;
                case "logout":
                    out.println("User " + selectedUser.getUsername() + " successfully logged out!");
                    selectedUser = null;
                    selectedPortfolioSnapshot = null;
                    users.save();
                    Help.noUser();
                    return false;
                case "exit":
                    users.save();
                    return true;
                default:
                    out.println("Invalid command, please try again.");
                    return false;
            }
        }
    }

    private boolean portfolioSelected() {
        switch (input.stringValue()) {
            case "selected":
                selected();
                return false;
            case "select":
                selectPortfolio();
                return false;
            case "add":
                selectedUser.addPortfolio();
                users.save();
                return false;
            case "buy":
                selectedPortfolioSnapshot.buy(availableSecurities, users);
                users.save();
                return false;
            case "sell":
                selectedPortfolioSnapshot.sell(users);
                users.save();
                return false;
            case "ov":
                selectedPortfolioSnapshot.overview(selectedPortfolioSnapshot);
                return false;
            case "lp":
                selectedUser.listPortfolios();
                return false;
            case "lf":
                availableSecurities.list();
                return false;
            case "ph":
                availableSecurities.priceHistory();
                return false;
            case "oh":
                selectedUser.printOrderHistory();
                return false;
            case "compare":
                if (selectedUser.hasPortfolios()) {
                    selectedUser.compare();
                } else {
                    out.println("Comparison is unavailable for users who own less than 2 portfolios.");
                }
                return false;
            case "help":
                return false;
            case "clear":
                Help.clear();
                return false;
            case "logout":
                selectedPortfolioSnapshot = null;
                selectedUser = null;
                return false;
            case "exit":
                users.save();
                return true;
            default:
                out.println("Invalid command, please try again.");
                return false;
        }
    }

    private void login(User user) {
        if (users.contains(user)) {
            out.println("Password: ");
            if (users.get(user.getUsername()).checkPassword(input.stringValue())) {
                selectUser(users.get(user.getUsername()));
                selectedUser.updatePortfolios(availableSecurities);
                out.println("User " + user.getUsername() + " is now logged in!");
                Help.clear();
            } else {
                out.println("Password invalid. Please try again.");
            }
        } else {
            out.println("User does not exist! Please register a new User");
        }
    }

    private void selected() {
        out.println("User selected:\t" + selectedUser.getUsername());
        if (selectedPortfolioSnapshot != null) {
            out.println("Depot selected:\t" + selectedPortfolioSnapshot.getName());
        } else {
            out.println("No depot selected");
        }
    }

    /**
     * Selects a user
     *
     * @param user User to be made selectedUser
     */
    public void selectUser(User user) {
        this.selectedUser = user;
    }

    private void selectPortfolio() {
        selectedUser.listPortfolios();
        out.println("Please enter the name of the portfolio you want to select.");
        String depotName = input.stringValue();
        if (selectedUser.hasPortfolio(depotName)) {
            selectedPortfolioSnapshot = selectedUser.getPortfolio(depotName);
            out.println("Depot " + depotName + " has been selected!");
        } else {
            out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }

    public static class Help {

        private Help() {}

        private static final String FORMAT = "%-15s %s%n";

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
            System.out.printf(FORMAT, "oh:", "Displays order history");
            System.out.printf(FORMAT, "help:", "Shows this dialog");
            System.out.printf(FORMAT, "clear:", "Clears the console");
            System.out.printf(FORMAT, "logout:", "Logs current user out and displays the login menu");
            System.out.printf(FORMAT, "exit:", "Exit Stocks");
            System.out.println();
        }

        static void portfolio() {
            System.out.println();
            System.out.printf(FORMAT, "selected:", "Overview of user & portfolio currently selected");
            System.out.printf(FORMAT, "select:", "Select an existing portfolio");
            System.out.printf(FORMAT, "buy:", "Add a new position to the selected portfolio");
            System.out.printf(FORMAT, "sell:", "Reduce an existing position in the selected portfolio");
            System.out.printf(FORMAT, "ov:", "Overview of all positions in selected portfolio");
            System.out.printf(FORMAT, "oh:", "Displays order history");
            System.out.printf(FORMAT, "compare:", "Compares two portfolios");
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