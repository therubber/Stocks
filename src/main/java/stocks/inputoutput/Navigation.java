package stocks.inputoutput;

import stocks.entities.Factory;
import stocks.entities.Portfolio;
import stocks.entities.User;
import stocks.entities.Security;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import java.util.InputMismatchException;

public class Navigation {

    private transient User selectedUser;
    private transient Portfolio selectedPortfolio;
    private static transient SecurityRepo availableSecurities = new SecurityRepo();
    private static UserRepo users = new UserRepo();
    private final Input input = new Input();
    private final Factory factory = new Factory();

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
                if (selectedPortfolio == null) {
                    Help.loggedIn();
                    exit = userNavigation();
                } else {
                    Help.portfolio();
                    exit = portfolioNavigation();
                }
            }
        }
    }

    private boolean noUserNavigation() {
        if (selectedUser == null) {
            switch (input.stringValue()) {
                case "login":
                    System.out.print("Please enter your username: ");
                    String username = input.stringValue();
                    login(factory.createUser(username));
                    return false;
                case "add":
                    addUser();
                    return false;
                case "lf":
                    availableSecurities.list();
                    return false;
                case "ph":
                    priceHistory();
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
                    System.out.println("Invalid command, please try again.");
                    return false;
            }
        }
        return false;
    }

    private boolean userNavigation() {
        if (selectedPortfolio != null) {
            return portfolioNavigation();
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
                    priceHistory();
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
                    System.out.println("User " + selectedUser.getUsername() + " successfully logged out!");
                    selectedUser = null;
                    selectedPortfolio = null;
                    users.save();
                    Help.noUser();
                    return false;
                case "exit":
                    users.save();
                    return true;
                default:
                    System.out.println("Invalid command, please try again.");
                    return false;
            }
        }
    }

    private boolean portfolioNavigation() {
        switch(input.stringValue()) {
            case "selected":
                selected();
                return false;
            case "select":
                selectPortfolio();
                return false;
            case "buy":
                selectedPortfolio.buy(availableSecurities, users);
                users.save();
                return false;
            case "sell":
                selectedPortfolio.sell(users);
                users.save();
                return false;
            case "ov":
                selectedPortfolio.overview(selectedPortfolio);
                return false;
            case "lp":
                selectedUser.listPortfolios();
                return false;
            case "lf":
                availableSecurities.list();
                return false;
            case "ph":
                priceHistory();
                return false;
            case "oh":
                selectedUser.printOrderHistory();
                return false;
            case "compare":
                if (selectedUser.hasPortfolios()) {
                    selectedUser.compare();
                } else {
                    System.out.println("Comparison is unavailable for users who own less than 2 portfolios.");
                }
                return false;
            case "help":
                return false;
            case "clear":
                Help.clear();
                return false;
            case "logout":
                selectedPortfolio = null;
                selectedUser = null;
                return false;
            case "exit":
                users.save();
                return true;
            default:
                System.out.println("Invalid command, please try again.");
                return false;
        }
    }

    private void login(User user) {
        if (users.contains(user)) {
            System.out.println("Password: ");
            if (users.get(user.getUsername()).checkPassword(input.stringValue())) {
                selectUser(users.get(user.getUsername()));
                selectedUser.updatePortfolios(availableSecurities);
                System.out.println("User " + user.getUsername() + " is now logged in!");
                Help.clear();
            } else {
                System.out.println("Password invalid. Please try again.");
            }
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }

    private void addUser() {
        System.out.println("Enter a username to create a new user: ");
        String username = input.stringValue();
        if (!users.contains(factory.createUser(username))) {
            System.out.println("Please enter a password: ");
            User user = factory.createUser(username, input.stringValue());
            users.add(user);
            System.out.println("New user " + username + " has been created!");
            login(user);
        } else {
            System.out.println("User with that username already exists, please login.");
        }
        users.save();
    }

    private void selected() {
        System.out.println("User selected:\t" + selectedUser.getUsername());
        if (selectedPortfolio != null) {
            System.out.println("Depot selected:\t" + selectedPortfolio.getName());
        } else {
            System.out.println("No depot selected");
        }
    }

    /**
     * Selects a user
     * @param user User to be made selectedUser
     */
    public void selectUser(User user) {
        this.selectedUser = user;
    }

    private void selectPortfolio() {
        selectedUser.listPortfolios();
        System.out.println("Please enter the name of the portfolio you want to select.");
        String depotName = input.stringValue();
        if (selectedUser.hasPortfolio(depotName)) {
            selectedPortfolio = selectedUser.getPortfolio(depotName);
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }

    private void priceHistory() {
        availableSecurities.listIndexed();
        System.out.println("Enter the index of the Security whose price history you want to display or 0 to exit:");
        try {
            int index = input.intValue();
            if (index == 0) {
                System.out.println("Going back to main menu...");
            } else if (index <= availableSecurities.size()) {
                Security selectedSecurity = availableSecurities.get(index - 1);
                selectedSecurity.priceHistory();
            } else {
                System.out.println("Index invalid. Please try again.");
                priceHistory();
            }
        } catch (InputMismatchException e) {
            System.out.println("Index invalid. Please try again.");
        }
    }
}