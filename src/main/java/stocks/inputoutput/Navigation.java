package stocks.inputoutput;

import stocks.entities.Portfolio;
import stocks.entities.User;
import stocks.entities.Security;
import stocks.repo.Securities;
import stocks.repo.Users;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

public class Navigation {

    private transient User selectedUser;
    private transient Portfolio selectedPortfolio;

    public static void main(String[] args) {
        Navigation instance = new Navigation();
        Securities.load();
        Users.load();
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
            switch (Input.stringValue()) {
                case "login":
                    System.out.print("Please enter your username: ");
                    String username = Input.stringValue();
                    login(username);
                    return false;
                case "add":
                    addUser();
                    save();
                    return false;
                case "lf":
                    Securities.list();
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
                    save();
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
            switch (Input.stringValue()) {
                case "selected":
                    selected();
                    return false;
                case "select":
                    selectPortfolio();
                    return false;
                case "add":
                    selectedUser.addPortfolio();
                    save();
                    return false;
                case "lp":
                    selectedUser.listPortfolios();
                    return false;
                case "lf":
                    Securities.list();
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
                    save();
                    Help.noUser();
                    return false;
                case "exit":
                    save();
                    return true;
                default:
                    System.out.println("Invalid command, please try again.");
                    return false;
            }
        }
    }

    private boolean portfolioNavigation() {
        switch(Input.stringValue()) {
            case "selected":
                selected();
                return false;
            case "select":
                selectPortfolio();
                return false;
            case "buy":
                selectedPortfolio.buy();
                save();
                return false;
            case "sell":
                selectedPortfolio.sell();
                save();
                return false;
            case "ov":
                selectedPortfolio.overview(selectedPortfolio);
                return false;
            case "compare":
                if (selectedUser.hasPortfolios()) {
                    selectedUser.compare();
                } else {
                    System.out.println("Comparison is unavailable for users who own less than 2 portfolios.");
                }
                return false;
            case "hist":
                try {
                    System.out.println("Enter a date. Format: YYYY-MM-DD");
                    selectedPortfolio.historical(Input.stringValue());
                } catch (DateTimeParseException e) {
                    System.out.println("Input date is invalid. Please try again.");
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
                return true;
            default:
                System.out.println("Invalid command, please try again.");
                return false;
        }
    }

    private void save() {
        Users.save();
    }

    private void login(String username) {
        if (Users.contains(new User(username))) {
            System.out.println("Password: ");
            if (Users.get(username).checkPassword(Input.stringValue())) {
                selectUser(Users.get(username));
                selectedUser.updatePortfolios();
                System.out.println("User " + username + " is now logged in!");
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
        String username = Input.stringValue();
        if (!Users.contains(username)) {
            System.out.println("Please enter a password: ");
            Users.add(new User(username, Input.stringValue()));
            System.out.println("New user " + username + " has been created!");
            login(username);
        } else {
            System.out.println("User with that username already exists, please login.");
        }
        Users.save();
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
        String depotName = Input.stringValue();
        if (selectedUser.hasPortfolio(depotName)) {
            selectedPortfolio = selectedUser.getPortfolio(depotName);
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }

    private void priceHistory() {
        Securities.listIndexed();
        System.out.println("Enter the index of the Security whose price history you want to display or 0 to exit:");
        try {
            int index = Input.intValue();
            if (index == 0) {
                System.out.println("Going back to main menu...");
            } else if (index <= Securities.size()) {
                Security selectedSecurity = Securities.get(index - 1);
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