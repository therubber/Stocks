package stocks.inputoutput;

import stocks.snapshots.PortfolioSnapshot;
import stocks.factories.UserFactory;
import stocks.repo.SecurityRepo;
import stocks.repo.UserRepo;
import stocks.utility.NavigationContainer;

public class Navigation {

    private static transient SecurityRepo availableSecurities = new SecurityRepo();
    private static UserRepo users = new UserRepo();
    private final Input input = new Input();
    private final Output output = new Output();
    private final UserFactory userFactory = new UserFactory(input, output);

    /**
     * Main method for the stocks program
     */
    public static void main(String[] args) {
        Navigation instance = new Navigation();
        availableSecurities.load();
        users.load();
        instance.navigation();
    }

    /**
     * Starts the navigation
     */
    private void navigation() {
        NavigationContainer navigationContainer = new NavigationContainer();
        while (!navigationContainer.getExit()) {
            if (navigationContainer.getUser() == null) {
                output.help.noUser();
                noUserNavigation(navigationContainer);
            } else {
                if (navigationContainer.getPortfolio() == null) {
                    output.help.loggedIn();
                    userSelected(navigationContainer);
                } else {
                    output.help.portfolio();
                    portfolioSelected(navigationContainer);
                }
            }
        }
    }

    /**
     * Controller for when no portfolio is selected
     *
     * @return boolean whether the program is to be terminated via -exit-
     */
    private NavigationContainer noUserNavigation(NavigationContainer navigationContainer) {
        switch (input.stringValue()) {
            case "login":
                output.print("Please enter your username: ");
                navigationContainer.setUser(userFactory.createUser(input.stringValue()));
                return login(navigationContainer);
            case "add":
                navigationContainer.setUser(users.addUser());
                return navigationContainer;
            case "lf":
                availableSecurities.listSecurities();
                return navigationContainer;
            case "ph":
                availableSecurities.priceHistory();
                return navigationContainer;
            case "help":
                return navigationContainer;
            case "clear":
                output.help.clear();
                return navigationContainer;
            case "exit":
                // users.save();
                navigationContainer.setExit(true);
                return navigationContainer;
            default:
                output.println("Invalid command, please try again.");
                return navigationContainer;
        }
    }

    /**
     * Controller for when a user is selected
     *
     * @return boolean whether the program is to be terminated via -exit-
     */
    private NavigationContainer userSelected(NavigationContainer navigationContainer) {
        if (navigationContainer.getPortfolio() != null) {
            return portfolioSelected(navigationContainer);
        } else {
            switch (input.stringValue()) {
                case "selected":
                    selected(navigationContainer);
                    return navigationContainer;
                case "select":
                    selectPortfolio(navigationContainer);
                    return navigationContainer;
                case "add":
                    navigationContainer.getUser().addPortfolio();
                    // users.save();
                    return navigationContainer;
                case "lp":
                    navigationContainer.getUser().listPortfolios();
                    return navigationContainer;
                case "lf":
                    availableSecurities.listSecurities();
                    return navigationContainer;
                case "ph":
                    availableSecurities.priceHistory();
                    return navigationContainer;
                case "help":
                    return navigationContainer;
                case "clear":
                    output.help.clear();
                    return navigationContainer;
                case "logout":
                    output.println("User " + navigationContainer.getUser().getUsername() + " successfully logged out!");
                    navigationContainer.setUser(null);
                    navigationContainer.setPortfolio(null);
                    // users.save();
                    output.help.noUser();
                    return navigationContainer;
                case "exit":
                    // users.save();
                    navigationContainer.setExit(true);
                    return navigationContainer;
                default:
                    output.println("Invalid command, please try again.");
                    return navigationContainer;
            }
        }
    }

    /**
     * Controller for when a portfolio is selected
     *
     * @return boolean whether the program is to be terminated via -exit-
     */
    private NavigationContainer portfolioSelected(NavigationContainer navigationContainer) {
        switch (input.stringValue()) {
            case "selected":
                selected(navigationContainer);
                return navigationContainer;
            case "select":
                selectPortfolio(navigationContainer);
                return navigationContainer;
            case "add":
                navigationContainer.getUser().addPortfolio();
                // users.save();
                return navigationContainer;
            case "buy":
                navigationContainer.getPortfolio().buy(availableSecurities, users);
                // users.save();
                return navigationContainer;
            case "sell":
                navigationContainer.getPortfolio().sell(users);
                // users.save();
                return navigationContainer;
            case "ov":
                navigationContainer.getPortfolio().overview(new PortfolioSnapshot(navigationContainer.getPortfolio()));
                return navigationContainer;
            case "oh":
                navigationContainer.getPortfolio().printOrderHistory();
                return navigationContainer;
            case "lp":
                navigationContainer.getUser().listPortfolios();
                return navigationContainer;
            case "lf":
                availableSecurities.listSecurities();
                return navigationContainer;
            case "ph":
                availableSecurities.priceHistory();
                return navigationContainer;
            case "vd":
                navigationContainer.getPortfolio().listHistory();
                System.out.println("Enter the state of the Portfolio from which you want to calculate the value increase: ");
                navigationContainer.getPortfolio().valueDevelopment(input.stringValue());
                return navigationContainer;
            case "compare":
                if (navigationContainer.getUser().hasPortfolios()) {
                    navigationContainer.getUser().compare();
                } else {
                    output.println("Comparison is unavailable for users who own less than 2 portfolios.");
                }
                return navigationContainer;
            case "hist":
                navigationContainer.getPortfolio().listHistory();
                System.out.println("Enter the state of the Portfolio you want to display: ");
                navigationContainer.getPortfolio().viewHistorical(input.stringValue());
                return navigationContainer;
            case "help":
                return navigationContainer;
            case "clear":
                output.help.clear();
                return navigationContainer;
            case "logout":
                navigationContainer.setPortfolio(null);
                navigationContainer.setUser(null);
                // users.save();
                return navigationContainer;
            case "exit":
                // users.save();
                navigationContainer.setExit(true);
                return navigationContainer;
            default:
                output.println("Invalid command, please try again.");
                return navigationContainer;
        }
    }

    /**
     * Used to log a user in / set the selectedUser
     * @param navigationContainer NavigationContainer to check user
     */
    private NavigationContainer login(NavigationContainer navigationContainer) {
        if (users.contains(navigationContainer.getUser())) {
            output.println("Password: ");
            if (users.get(navigationContainer.getUser().getUsername()).checkPassword(input.stringValue())) {
                navigationContainer.getUser().updatePortfolios(availableSecurities);
                output.println("User " + navigationContainer.getUser().getUsername() + " is now logged in!");
                navigationContainer.setUser(users.get(navigationContainer.getUser().getUsername()));
                output.help.clear();
                return navigationContainer;
            } else {
                output.println("Password invalid. Please try again.");
                navigationContainer.setUser(null);
                return navigationContainer;
            }
        } else {
            output.println("User does not exist! Please register a new User");
            navigationContainer.setUser(null);
            return navigationContainer;
        }
    }

    /**
     * Shows which user and Portfolio are selected
     */
    private void selected(NavigationContainer navigationContainer) {
        output.println("User selected:\t" + navigationContainer.getUser().getUsername());
        if (navigationContainer.getPortfolio() != null) {
            output.println("Depot selected:\t" + navigationContainer.getPortfolio().getName());
        } else {
            output.println("No depot selected");
        }
    }

    /**
     * Selects a portfolio in navigation from the selectedUsers list of portfolios
     */
    void selectPortfolio(NavigationContainer navigationContainer) {
        navigationContainer.getUser().listPortfolios();
        output.println("Please enter the name of the portfolio you want to select.");
        String depotName = input.stringValue();
        if (navigationContainer.getUser().hasPortfolio(depotName)) {
            navigationContainer.setPortfolio(navigationContainer.getUser().getPortfolio(depotName));
            output.println("Depot " + navigationContainer.getPortfolio().getName() + " has been selected!");
        } else {
            output.println("Depot " + depotName + " does not exist or isn't owned by you, please try a different depot.");
        }
    }
}