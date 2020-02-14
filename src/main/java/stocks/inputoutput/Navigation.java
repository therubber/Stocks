package stocks.inputoutput;

import stocks.entities.Portfolio;
import stocks.entities.User;
import stocks.dows.SecurityDow;
import stocks.repo.Securities;
import stocks.repo.Users;
import java.io.ByteArrayInputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Navigation {

    public transient User selectedUser;
    private transient Portfolio selectedPortfolio;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Navigation instance = new Navigation();
        Users.load();
        Users.printUsers();
        Securities.load();
        Help.noUser();
        instance.navigation();
    }

    public void navigation() {
        boolean exit = false;
        while (!exit) {
            if (selectedUser == null) {
                exit = noUserNavigation();
            } else {
                exit = userNavigation();
            }
        }
        scanner.close();
    }

    private boolean noUserNavigation() {
        if (selectedUser == null) {
            switch (scanner.next()) {
                case "login":
                    System.out.print("Please enter your username: ");
                    String username = scanner.next();
                    login(username);
                    return false;
                case "add":
                    addUser();
                    return false;
                case "lf":
                    Securities.list();
                    return false;
                case "ph":
                    priceHistory();
                    return false;
                case "help":
                    Help.noUser();
                    return false;
                case "clear":
                    Help.clear();
                    return false;
                case "exit":
                    save();
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean userNavigation() {
            switch (scanner.next()) {
                case "selected":
                    selected();
                    return false;
                case "select":
                    selectPortfolio();
                    return false;
                case "add":
                    selectPortfolio(selectedUser.addPortfolio());
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
                case "buy":
                    if (selectedPortfolio != null) {
                        selectedPortfolio.buy();
                    } else {
                        System.out.println("No portfolio selected! Please select a portfolio using the -select- command.");
                    }
                    return false;
                case "sell":
                    if (selectedPortfolio != null) {
                        selectedPortfolio.sell();
                    } else {
                        System.out.println("No portfolio selected! Please select a portfolio using the -select- command.");
                    }
                    return false;
                case "ov":
                    if (selectedPortfolio != null) {
                        selectedPortfolio.overview();
                    } else {
                        System.out.println("No portfolio selected, Please select one using the -select- command.");
                    }
                    return false;
                case "oh":
                    selectedUser.printOrderHistory();
                    return false;
                case "compare":
                    selectedUser.compare();
                    return false;
                case "help":
                    Help.loggedIn();
                    return false;
                case "clear":
                    Help.clear();
                    return false;
                case "logout":
                    System.out.println("User " + selectedUser.getUsername() + " successfully logged out!");
                    selectedUser = null;
                    selectedPortfolio = null;
                    Help.noUser();
                    return false;
                case "exit":
                    save();
                    return true;
                default:
                    return false;
            }
    }

    private void save() {
        Users.save();
    }

    private void login(String username) {
        if (Users.getAll().contains(new User(username))) {
            selectedUser = Users.get(username);
            System.out.println("User " + username + " is now logged in!");
            for (Portfolio portfolio : selectedUser.getPortfolios()) {
                portfolio.update();
            }
            Help.clear();
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }

    private void addUser() {
        System.out.println("Enter a username to create a new user: ");
        String username = scanner.next();
        if (!Users.contains(username)) {
            Users.add(new User(username));
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

    private void selectPortfolio() {
        System.out.println("Please enter the name of the portfolio you want to select.");
        String depotName = scanner.next();
        if (selectedUser.hasPortfolio(new Portfolio(depotName, selectedUser.toString()))) {
            selectedPortfolio = selectedUser.getPortfolios().get(selectedUser.getPortfolios().indexOf(new Portfolio(depotName, selectedUser.toString())));
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }

    private void selectPortfolio(String name) {
        selectedPortfolio = selectedUser.getPortfolio(new Portfolio(name, selectedUser.toString()));
        System.out.println("Portfolio " + name + " has been selected!");
    }

    private void priceHistory() {
        int number = Securities.listIndexed();
        System.out.println("Enter the index of the Security whose price history you want to display or 0 to exit:");
        try {
            int index = scanner.nextInt();
            if (index == 0) {
                System.out.println("Going back to main menu...");
            } else if (index < number) {
                SecurityDow selectedSecurity = Securities.get(index - 1);
                selectedSecurity.priceHistory();
            } else {
                System.out.println("Index invalid. Please try again.");
                priceHistory();
            }
        } catch (InputMismatchException e) {
            System.out.println("Index invalid. Please try again.");
        }
    }

    /**
     * Method to set System.in to use the String of the parameter
     * @param input String commands to be executed
     */
    public void test(String input) {
        ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(testInput);
    }
}