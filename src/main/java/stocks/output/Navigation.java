package stocks.output;

import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.dows.FundDow;
import stocks.interfaces.Fund;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Navigation {

    private User selectedUser;
    private Portfolio selectedPortfolio;
    private Scanner scanner = new Scanner(System.in);
    private LinkedList<User> users = new LinkedList<>();
    private LinkedList<FundDow> funds = new LinkedList<>();

    public static void main(String[] args) {
        Navigation navigation = new Navigation();
        navigation.loadFunds();
        Output.normal();
        String navCurrent = navigation.navigation();
        while (!navCurrent.equals("exit")) {
            if (navigation.selectedUser == null) {
                navCurrent = navigation.navigation();
            } else {
                navCurrent = navigation.userNavigation();
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
            case "lf":
                listFunds();
                return "lf";
            case "help":
                Output.normal();
                return "help";
            case "clear":
                Output.clear();
                return "clear";
            case "exit":
                // TODO: Speichern vor dem beenden.
                return "exit";
            default:
                return "help";
        }
    }

    public String userNavigation() {
        String input = scanner.next();
        switch (input) {
            case "selected":
                selected();
                return "selected";
            case "select":
                selectPortfolio();
                return "select";
            case "add":
                addPortfolio();
                return "add";
            case "ld":
                listPortfolios();
                return "ld";
            case "lf":
                listFunds();
                return "lf";
            case "buy":
                if (selectedPortfolio != null) {
                    buy();
                } else {
                    System.out.println("No portfolio selected! Please select a portfolio using the -select- command.");
                }
                return "buy";
            case "sell":
                if (selectedPortfolio != null) {
                    sell();
                } else {
                    System.out.println("No portfolio selected! Please select a portfolio using the -select- command.");
                }
                return "sell";
            case "ov":
                if (selectedPortfolio != null) {
                    selectedPortfolio.overview();
                } else {
                    System.out.println("No portfolio selected, Please select one using the -select- command.");
                }
                return "overview";
            case "help":
                Output.user();
                return "help";
            case "clear":
                Output.clear();
                return "clear";
            case "logout":
                System.out.println("User " + selectedUser.getName() + " successfully logged out!");
                selectedUser = null;
                selectedPortfolio = null;
                Output.normal();
                return "logout";
            case "exit":
                // TODO: Speichern vor dem beenden.
                return "exit";
            default:
                return "help";
        }
    }

    private void listFunds() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-4s%n", "Name", "ISIN", "WKN", "Spot Price");
        for (FundDow fundDow : funds) {
            System.out.printf("%-18s %-16s %-10s %.2f %n", fundDow.getName(), fundDow.getIsin(), fundDow.getWkn(), fundDow.getSpotPrice());
        }
        System.out.println();
    }

    private void login(String username) {
        if (users.contains(new User(username)) && !username.equals("unlogged")) {
            selectedUser = users.get(users.indexOf(new User(username)));
            System.out.println("User " + username + " is now logged in!");
            Output.clear();
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }

    /**
     * Sets up names of available funds and fetches data using updateFunds()
     */
    private void loadFunds() {
        funds.add(new FundDow("UniRAK"));
        funds.add(new FundDow("UniEuroAnleihen"));
        funds.add(new FundDow("GenoAs"));
        funds.add(new FundDow("UniAsia"));
        updateFunds();
    }

    private void updateFunds() {
        System.out.print("Updating spot prices...");
        for (Fund fund : funds) {
            try {
                fund.update();
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe.toString());
            }
        }
        System.out.println(" Done!");
    }

    private void addUser() {
        System.out.println("Enter a username to create a new user: ");
        String name = scanner.next();
        if (!users.contains(new User(name))) {
            users.add(new User(name));
            System.out.println("New user " + name + " has been created!");
        } else {
            System.out.println("User with that username already exists, please login.");
        }
    }

    private void addPortfolio() {
        System.out.println("User equity: " + selectedUser.getEquity() + "EUR");
        System.out.println("Enter a Name for the portfolio you want to create: ");
        String name = scanner.next();
        System.out.println("Enter the amount of equity to transfer to the portfolio account: ");
        double depotEquity = scanner.nextDouble();
        if (depotEquity <= selectedUser.getEquity()) {
            selectedUser.portfolios.add(new Portfolio(name, selectedUser, depotEquity));
            selectedUser.setEquity(selectedUser.getEquity() - depotEquity);
            System.out.println("Depot " + name + " successfully created!");
        } else {
            System.out.println("Insufficient account equity for depot creation! please try again");
        }
    }

    private void listPortfolios() {
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
        int transactionCount = scanner.nextInt();
        Position position = new Position(transactionCount, fund);
        if (position.getValue() <= selectedPortfolio.getEquity()) {
            System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", selectedPortfolio.getEquity(), (selectedPortfolio.getEquity() - position.getValue()));
            System.out.println("Buying " + transactionCount + " shares of " + fund.getName() + " at " + fund.getSpotPrice() + " EUR Spot. Confirm (y/n)");
            String input = scanner.next();
            if (input.equals("y")) {
                selectedPortfolio.addPosition(position);
                selectedPortfolio.changeEquity(-position.getValue());
                System.out.println("Buy order successfully executed! New portfolio equity: " + selectedPortfolio.getEquity());
            } else {
                System.out.println("Buy order cancelled, back to menu.");
            }
        } else {
            System.out.println("Insufficient balance! Please try ordering fewer shares.");
        }
    }

    public void sell() {
        selectedPortfolio.indexPositions();
        System.out.println("Please select the position you want to reduce by entering its index.");
        int index = scanner.nextInt();
        if (index <= selectedPortfolio.getPositionCount() && index > 0) {
            Position selectedPosition = selectedPortfolio.getPosition(index - 1);
            System.out.println("Enter the amount of shares you want to reduce/increase the position by");
            int transactionCount = scanner.nextInt();
            if (transactionCount <= selectedPosition.getCount()) {
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", selectedPortfolio.getEquity(), (selectedPortfolio.getEquity() - (selectedPosition.getValue()) - transactionCount * selectedPosition.getFund().getSpotPrice()));
                System.out.println("Selling " + transactionCount + " shares of " + selectedPosition.getFundName() + " at " + selectedPosition.getFund().getSpotPrice() + " EUR Spot. Confirm (y/n)");
                String confirm = scanner.next();
                if (confirm.equals("y")) {
                    selectedPortfolio.changeEquity(selectedPosition.changeCount(transactionCount));
                    System.out.println("Sell order successfully executed! New portfolio equity: " + selectedPortfolio.getEquity());
                    if (selectedPosition.getCount() == 0) {
                        selectedPortfolio.deletePosition(selectedPosition);
                    }
                } else {
                    System.out.println("Sell order cancelled, back to menu.");
                }
            } else {
                System.out.println("Amount of shares to sell exceeds amount of shares owned. Please try again.");
            }
        } else {
            System.out.println("The selected position does not exist, please try again.");
        }
    }

    private void selected() {
        System.out.println("User selected:\t" + selectedUser.getName());
        if (selectedPortfolio != null) {
            System.out.println("Depot selected:\t" + selectedPortfolio.getName());
        } else {
            System.out.println("No depot selected");
        }
    }

    private void selectPortfolio() {
        System.out.println("Please enter the name of the portfolio you want to select.");
        String depotName = scanner.next();
        if (selectedUser.portfolios.contains(new Portfolio(depotName, selectedUser))) {
            selectedPortfolio = selectedUser.portfolios.get(selectedUser.portfolios.indexOf(new Portfolio(depotName, selectedUser)));
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }
}