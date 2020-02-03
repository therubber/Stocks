package stocks;

import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.dows.FundDow;

import java.util.LinkedList;
import java.util.Scanner;

public class Broker {

    private User selectedUser;
    private Portfolio selectedPortfolio;
    private Scanner scanner = new Scanner(System.in);
    private LinkedList<User> users = new LinkedList<>();
    private LinkedList<FundDow> funds = new LinkedList<>();

    public static void main(String[] args) {
        Broker broker = new Broker();
        broker.loadFunds();
        broker.help();
        String navCurrent = broker.navigation();
        while (navCurrent != "exit") {
            if (broker.selectedUser == null) {
                navCurrent = broker.navigation();
            } else {
                navCurrent = broker.userNavigation();
            }
        }
    }

    public String navigation() {
        String input = scanner.next();
        switch (input) {
            case "login":
                System.out.print("Please enter your username: ");
                String username = scanner.next();
                login(username);
                return "login";
            case "add":
                addUser();
                return "add";
            case "help":
                help();
                return "help";
            case "clear":
                clear();
                return "clear";
            case "exit":
                return "exit";
            default:
                return "help";
        }
    }

    public String userNavigation() {
        String input = scanner.next();
        switch (input) {
            case "selected":
                selectedDepot();
                return "selected";
            case "select":
                selectDepot();
                return "select";
            case "add":
                addDepot();
                return "add";
            case "ld":
                listDepots();
                return "ld";
            case "lf":
                listFunds();
                return "lf";
            case "buy":
                if (selectedPortfolio != null) {
                    buy();
                } else {
                    System.out.println("No Depot selected! Please select a depot using the -select- command.");
                }
                return "buy";
            case "listpos":
                selectedPortfolio.positions();
                return "listpos";
            case "overview":
                selectedPortfolio.overview();
                return "overview";
            case "help":
                userHelp();
                return "help";
            case "clear":
                clear();
                return "clear";
            case "logout":
                System.out.println("User " + selectedUser.getName() + " successfully logged out!");
                selectedUser = null;            // main checks whether selectedUser = null to see which menu to display
                help();
                return "logout";
            case "exit":
                return "exit";
            default:
                return "help";
        }
    }

    private void help() {
        System.out.println("login: \t Log in with existing user");
        System.out.println("add: \t Register a new user");
        System.out.println("help: \t Shows this dialog");
        System.out.println("clear: \t Clears the console");
        System.out.println("exit: \t Exit Stocks");
    }

    private void userHelp() {
        System.out.println("selected: \t Overview of user & depot currently selected");
        System.out.println("select: \t Select an existing depot");
        System.out.println("add: \t\t Add a new depot");
        System.out.println("ld: \t\t List all depots");
        System.out.println("lf: \t\t List all funds");
        System.out.println("buy: \t\t Add a new position to the selected depot");
        System.out.println("listpos: \t Overview of all positions in selected depot");
        System.out.println("help: \t\t Shows this dialog");
        System.out.println("clear: \t\t Clears the console");
        System.out.println("logout: \t\t Logs current user out and displays the login menu");
        System.out.println("exit: \t\t Exit Stocks");
    }

    private void listFunds() {
        System.out.println(System.lineSeparator() + "Name\t\t" + "ISIN\t" + "WKN\t" + "Spot Price");
        for (FundDow fundDow : funds) {
            System.out.println(fundDow.getName() + "\t" + fundDow.getIsin() + "\t" + fundDow.getWkn() + "\t" + fundDow.getSpotPrice());
        }
    }

    private void login(String username) {
        if (users.contains(new User(username)) && username != "unlogged") {
            selectedUser = users.get(users.indexOf(new User(username)));
            System.out.println("User " + username + " is now logged in!");
            clear();
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }

    // Hardcode setup for Funds, read from File in Future versions
    private void loadFunds() {
        funds.add(new FundDow("GenoAs: 1", "DE0009757682", "975768", 87.91));
        funds.add(new FundDow("UniAsia", "LU0037079034", "971267", 76.78));
        funds.add(new FundDow("UniEuroAnleihen", "LU0966118209", "A1W4QB", 57.51));
        funds.add(new FundDow("UniRAK", "DE0008491044", "849104", 134.64));
    }

    private void clear() {
        for (int i = 0; i <= 40; i++) {
            System.out.println("");
        }
    }

    private void addUser() {
        System.out.print("Enter a username to create a new user: ");
        String name = scanner.next();
        if (!users.contains(new User(name))) {
            users.add(new User(name));
            System.out.println("New user " + name + " has been created!");
        } else {
            System.out.println("User with that username already exists, please login.");
        }
    }

    private void addDepot() {
        System.out.println("User equity: " + selectedUser.getEquity() + "EUR");
        System.out.println("Enter a Name for the depot you want to create: ");
        String name = scanner.next();
        System.out.println("Enter the amount of equity to transfer to the depot account: ");
        double depotEquity = scanner.nextDouble();
        if (depotEquity <= selectedUser.getEquity()) {
            selectedUser.portfolios.add(new Portfolio(name, selectedUser, depotEquity));
            selectedUser.setEquity(selectedUser.getEquity() - depotEquity);
            System.out.println("Depot " + name + " successfully created!");
        } else {
            System.out.println("Insufficient account equity for depot creation! please try again");
        }
    }

    private void listDepots() {
        if (!selectedUser.portfolios.isEmpty()) {
            selectedUser.listDepots();
        } else {
            System.out.println("No depots existing for user " + selectedUser.getName() + ". Please add a new one.");
        }
    }

    private void buy() {
        listFunds();
        System.out.println("Depot equity: " + selectedPortfolio.getEquity() + " EUR");
        System.out.println("Enter the name of the Fund that you want to buy: ");
        String fundName = scanner.next();
        if (!funds.contains(new FundDow(fundName))) {
            System.out.println("Fund not available, please try again.");
            buy();
        }
        FundDow fund = funds.get(funds.indexOf(new FundDow(fundName)));
        System.out.println("Enter the count of shares you want to buy: ");
        int count = scanner.nextInt();
        Position position = new Position(count, fund);
        System.out.println("current equity: " + selectedPortfolio.getEquity() + " EUR, remaining after execution: " + (selectedPortfolio.getEquity() - position.getValue()) + " EUR");
        if (position.getValue() <= selectedPortfolio.getEquity()) {
            System.out.println("Do you want to place the order? (y/n)");
            String input = scanner.next();
            if (input.equals("y")) {
                selectedPortfolio.addPosition(position);
                selectedPortfolio.setEquity(selectedPortfolio.getEquity() - position.getValue());
                System.out.println("Buy order successfully executed! New depot equity: " + selectedPortfolio.getEquity());
            } else {
                System.out.println("Buy order aborted, back to menu");
            }
        } else {
            System.out.println("Insufficient balance! Please try ordering fewer shares.");
        }
    }

    private void sell() {

    }

    private void selectedDepot() {
        System.out.println("User selected:\t" + selectedUser.getName());
        if (selectedPortfolio != null) {
            System.out.println("Depot selected:\t" + selectedPortfolio.getName());
        } else {
            System.out.println("No depot selected");
        }
    }

    private void selectDepot() {
        String depotName = scanner.next();
        if (selectedUser.portfolios.contains(new Portfolio(depotName, selectedUser))) {
            selectedPortfolio = selectedUser.portfolios.get(selectedUser.portfolios.indexOf(new Portfolio(depotName, selectedUser)));
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }
}