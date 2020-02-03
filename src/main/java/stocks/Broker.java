package stocks;

import stocks.entities.Depot;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.dows.FundDow;

import java.util.LinkedList;
import java.util.Scanner;

public class Broker {

    private User selectedUser;
    private Depot selectedDepot;
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
        System.out.print("Enter: ");
        String input = scanner.next();
        switch (input) {
            case "login":
                System.out.print("Please enter your username: ");
                String username = scanner.next();
                login(username);
                return "login";
            case "add":
                System.out.print("Enter a username to create a new user: ");
                String name = scanner.next();
                if (!users.contains(new User(name))) {
                    users.add(new User(name));
                    System.out.println("New user " + name + " has been created!");
                } else {
                    System.out.println("User with that username already exists, please login.");
                }
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
        System.out.print("Enter: ");
        String input = scanner.next();
        switch (input) {
            case "select":
                // Selects depot with the name entered by the user if it is existent in the list of his depots
                String depotName = scanner.next();
                if (selectedUser.depots.contains(new Depot(depotName, selectedUser))) {
                    selectedDepot = selectedUser.depots.get(selectedUser.depots.indexOf(new Depot(depotName, selectedUser)));
                    clear();
                    System.out.println("Depot " + depotName + " has been selected!");
                } else {
                    System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
                }
                return "select";
            case "add":
                System.out.println("User equity: " + selectedUser.getEquity() + "EUR");
                System.out.println("Enter a Name for the depot you want to create: ");
                String name = scanner.next();
                System.out.println("Enter the amount of equity to transfer to the depot account: ");
                double depotEquity = scanner.nextDouble();
                if (depotEquity <= selectedUser.getEquity()) {
                    selectedUser.depots.add(new Depot(name, selectedUser, depotEquity));
                } else {
                    System.out.println("Insufficient account equity for depot creation! please try again");
                }
                return "add";
            case "ld":
                if (!selectedUser.depots.isEmpty()) {
                    selectedUser.listDepots();
                } else {
                    System.out.println("No depots existing for user " + selectedUser.username + ". Please add a new one.");
                }
                return "ld";
            case "lf":
                listFunds();
                return "lf";
            case "buy":
                if (selectedDepot != null) {
                    buy();
                } else {
                    System.out.println("No Depot selected! Please select a depot using the -select- command.");
                }
                return "buy";
            case "listpos":
                selectedDepot.positions();
                return "listpos";
            case "help":
                userHelp();
                return "help";
            case "clear":
                for (int i = 0; i <= 15; i++) {
                    System.out.println("");
                }
                return "clear";
            case "logout":
                selectedUser = null;            // main checks whether selectedUser = null to see which menu to display
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
        System.out.println("select: \t Select an existing depot");
        System.out.println("add: \t\t Add a new depot");
        System.out.println("ld: \t\t List all depots");
        System.out.println("lf: \t\t List all funds");
        System.out.println("buy: \t\t Add a new position to the selected depot");
        System.out.println("help: \t\t Shows this dialog");
        System.out.println("clear: \t\t Clears the console");
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
        for (int i = 0; i <= 15; i++) {
            System.out.println("");
        }
    }

    private void buy() {
        listFunds();
        System.out.println("Depot equity: " + selectedDepot.getEquity() + " EUR");
        System.out.println("Enter the name of the Fund that you want to buy: ");
        String fundName = scanner.next();
        FundDow fund = funds.get(funds.indexOf(new FundDow(fundName)));
        System.out.println("Enter the count of shares you want to buy: ");
        int count = scanner.nextInt();
        Position position = new Position(count, fund);
        if (position.getValue() <= selectedDepot.getEquity()) {
            selectedDepot.addPosition(position);
            selectedDepot.setEquity(selectedDepot.getEquity() - position.getValue());
            System.out.println("Buy order successfully executed! New depot equity: " + selectedDepot.getEquity());
        } else {
            System.out.println("Insufficient balance! Please try ordering fewer shares.");
        }
    }
}