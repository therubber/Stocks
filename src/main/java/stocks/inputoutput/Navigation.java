package stocks.inputoutput;

import stocks.dows.SpotPrice;
import stocks.entities.Order;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;
import stocks.dows.SecurityDow;
import stocks.interfaces.Security;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Navigation {

    private User selectedUser;
    private Portfolio selectedPortfolio;
    private final Scanner scanner = new Scanner(System.in);
    private List<User> users = new LinkedList<>();
    private List<Security> securities = new LinkedList<>();

    /**
     * Getter method for user list
     * @return List containing all registered users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Setter method for user list
     * @param users List containing users to be registered
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Getter method for list of available securities
     * @return List of security objects
     */
    public List<Security> getSecurities() {
        return securities;
    }

    /**
     * Setter method for available securities
     * @param securities List containing all securities to be made available
     */
    public void setSecurities(List<Security> securities) {
        this.securities = securities;
    }

    public static void main(String[] args) {
        Navigation currentInstance = IO.Load.fromJson();
        IO.Help.noUser();
        String navCurrent = currentInstance.navigation();
        while (!navCurrent.equals("exit")) {
            if (currentInstance.selectedUser == null) {
                navCurrent = currentInstance.navigation();
            } else {
                navCurrent = currentInstance.userNavigation();
            }
        }
        currentInstance.save();
    }

    private String navigation() {
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
                listSecurities();
                return "lf";
            case "ph":
                priceHistory();
                return "ph";
            case "help":
                IO.Help.noUser();
                return "help";
            case "clear":
                IO.Help.clear();
                return "clear";
            case "exit":
                return "exit";
            default:
                return "help";
        }
    }

    private String userNavigation() {
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
            case "lp":
                listPortfolios();
                return "ld";
            case "lf":
                listSecurities();
                return "lf";
            case "ph":
                priceHistory();
                return "ph";
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
            case "oh":
                selectedUser.orderHistory();
                return "oh";
            case "help":
                IO.Help.loggedIn();
                return "help";
            case "clear":
                IO.Help.clear();
                return "clear";
            case "logout":
                System.out.println("User " + selectedUser.getUsername() + " successfully logged out!");
                selectedUser = null;
                selectedPortfolio = null;
                IO.Help.noUser();
                return "logout";
            case "exit":
                return "exit";
            default:
                return "help";
        }
    }

    private void save() {
        IO.Save.toJson(this);
    }

    private void listSecurities() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-10s %-15s%n", "Name", "ISIN", "WKN", "Price", "Date");
        System.out.println();
        for (Security security : securities) {
            System.out.printf("%-18s %-16s %-10s %-10.2f %-15s%n", security.getName(), security.getIsin(), security.getWkn(), security.getSpotPrice().getPrice(), security.getSpotDate());
        }
        System.out.println();
    }

    private void login(String username) {
        if (users.contains(new User(username)) && !username.equals("unlogged")) {
            selectedUser = users.get(users.indexOf(new User(username)));
            System.out.println("User " + username + " is now logged in!");
            IO.Help.clear();
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }

    private void addUser() {
        System.out.println("Enter a username to create a new user: ");
        String name = scanner.next();
        if (!users.contains(new User(name))) {
            users.add(new User(name));
            System.out.println("New user " + name + " has been created!");
            login(name);
        } else {
            System.out.println("User with that username already exists, please login.");
        }
        save();
    }

    private void addPortfolio() {
        System.out.println("User equity: " + selectedUser.getEquity() + "EUR");
        System.out.println("Enter a Name for the portfolio you want to create: ");
        String name = scanner.next();
        System.out.println("Enter the amount of equity to transfer to the portfolio account: ");
        BigDecimal depotEquity = BigDecimal.valueOf(scanner.nextDouble());
        if (depotEquity.doubleValue() <= selectedUser.getEquity().doubleValue()) {
            selectedUser.portfolios.add(new Portfolio(name, selectedUser.toString(), depotEquity));
            selectedUser.setEquity(selectedUser.getEquity().subtract(depotEquity));
            selectPortfolio(name);
            System.out.println("Depot " + name + " successfully created!");
        } else {
            System.out.println("Insufficient account equity for depot creation! please try again");
        }
        save();
    }

    private void listPortfolios() {
        System.out.println();
        if (selectedUser != null) {
            if (!selectedUser.portfolios.isEmpty()) {
                selectedUser.listPortfolios();
            } else {
                System.out.println("No portfolios existing for user " + selectedUser.getUsername() + ". Please add a new one.");
            }
        } else {
            System.out.println("No user selected, please log in!");
        }
    }

    private void buy() {
        if (evaluateSpotPrices()) {
            listSecurities();
            System.out.println("Depot equity: " + selectedPortfolio.getEquity() + " EUR");
            System.out.println("Enter the name of the Security that you want to buy: ");
            String securityName = scanner.next();
            if (!securities.contains(new SecurityDow(securityName))) {
                System.out.println("Security not available, please try again.");
                buy();
            }
            Security security = securities.get(securities.indexOf(new SecurityDow(securityName)));
            System.out.println("Enter the count of shares you want to buy: ");
            int transactionCount = scanner.nextInt();
            Position position = new Position(transactionCount, security);
            if (position.getValue().doubleValue() <= selectedPortfolio.getEquity().doubleValue()) {
                System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", selectedPortfolio.getEquity(), (selectedPortfolio.getEquity().subtract(position.getValue())));
                System.out.println("Buying " + transactionCount + " shares of " + security.getName() + " at " + security.getSpotPrice().getPrice().setScale(2, RoundingMode.CEILING) + " EUR Spot. Confirm (y/n)");
                String input = scanner.next();
                if (input.equals("y")) {
                    selectedPortfolio.addPosition(position);
                    selectedPortfolio.setEquity(selectedPortfolio.getEquity().subtract(position.getValue()));
                    selectedUser.getOrderHistory().add(new Order(transactionCount, security.getSpotPrice().getPrice(), LocalDate.now().toString(), "BUY", security));
                    if (!selectedPortfolio.ownedSecurities.contains(security)) {
                        selectedPortfolio.ownedSecurities.add(security);
                    }
                    System.out.println("Buy order successfully executed! New portfolio equity: " + selectedPortfolio.getEquity().setScale(2, RoundingMode.CEILING));
                } else {
                    System.out.println("Buy order cancelled, back to menu.");
                }
            } else {
                System.out.println("Insufficient balance! Please try ordering fewer shares.");
            }
        } else {
            System.out.println("Spot prices were not updated properly. Please try again later.");
        }
        save();
    }

    private void sell() {
        if (evaluateSpotPrices()) {
            selectedPortfolio.indexPositions();
            System.out.println("Please select the position you want to reduce by entering its index.");
            int index = scanner.nextInt();
            if (index <= selectedPortfolio.getPositionCount() && index > 0) {
                Position selectedPosition = selectedPortfolio.getPosition(index - 1);
                System.out.println("Enter the amount of shares you want to reduce/increase the position by");
                int transactionCount = scanner.nextInt();
                if (transactionCount <= selectedPosition.getCount()) {
                    System.out.printf("Current equity: %10.2f EUR. Remaining after execution: %10.2f%n", selectedPortfolio.getEquity(), (selectedPortfolio.getEquity().add(selectedPosition.getValue()).subtract(selectedPosition.getSpotPrice().getPrice().multiply(new BigDecimal(transactionCount)))));
                    System.out.println("Selling " + transactionCount + " shares of " + selectedPosition.getSecurityName() + " at " + selectedPosition.getSpotPrice().getPrice().setScale(2, RoundingMode.CEILING) + " EUR Spot. Confirm (y/n)");
                    if (confirmOrder()) {
                        selectedPosition.setCount(selectedPosition.getCount() - transactionCount);
                        selectedUser.getOrderHistory().add(new Order(transactionCount, selectedPosition.getSecurity().getSpotPrice().getPrice(), LocalDate.now().toString(), "SELL", selectedPosition.getSecurity()));
                        selectedPortfolio.setEquity(selectedPortfolio.getEquity().add(selectedPosition.getSpotPrice().getPrice().multiply(new BigDecimal(transactionCount))));
                        System.out.println("Sell order successfully executed! New portfolio equity: " + selectedPortfolio.getEquity().setScale(2, RoundingMode.CEILING));
                        if (selectedPosition.isZero()) {
                            selectedPortfolio.deletePosition(selectedPosition);
                            selectedPortfolio.ownedSecurities.remove(selectedPosition.getSecurity());
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
        } else {
            System.out.println("Spot prices were not updated properly. Please try again later.");
        }
        save();
    }

    private boolean evaluateSpotPrices() {
        boolean validPrices = false;
        if (!securities.isEmpty()) {
            validPrices = true;
            for (Security security : securities) {
                if (security.getSpotPrice().getDate().equals("UPDATE ERROR")) {
                    validPrices = false;
                }
            }
        }
        return validPrices;
    }

    private boolean confirmOrder() {
        String input = scanner.next();
        return input.equals("y");
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
        if (selectedUser.portfolios.contains(new Portfolio(depotName, selectedUser.toString()))) {
            selectedPortfolio = selectedUser.portfolios.get(selectedUser.portfolios.indexOf(new Portfolio(depotName, selectedUser.toString())));
            System.out.println("Depot " + depotName + " has been selected!");
        } else {
            System.out.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }

    private void selectPortfolio(String name) {
        selectedPortfolio = selectedUser.portfolios.get(selectedUser.portfolios.indexOf(new Portfolio(name, selectedUser.toString())));
        System.out.println("Portfolio " + name + " has been selected!");
    }

    private void priceHistory() {
        listSecurities();
        System.out.println("Enter the name of the Security whose price history you want to display:");
        Security selectedSecurity = securities.get(securities.indexOf(new SecurityDow(scanner.next())));
        if (!selectedSecurity.getHistoricalPrices().isEmpty()) {
            System.out.printf("%-15s %-10s%n", "Date", "Price");
            for (SpotPrice spotPrice : selectedSecurity.getHistoricalPrices()) {
                System.out.printf("%-15s %-10.2f%n", spotPrice.getDate(), spotPrice.getPrice());
            }
        } else {
            System.out.println("No historical prices found, please update prices.");
        }
    }
}