package stocks.inputoutput;

import stocks.entities.Position;

import java.util.Scanner;

public class Help {

    static final String FORMAT = "%-15s %s%n";

    /** Private Constructor to hide public one */
    private Help() {}

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

    public static boolean confirmOrder(Position position, boolean type) {
        if (type) {
            System.out.println("Buying " + position.getCount() + " shares of " + position.getSecurity().getName() + " at " + position.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        } else {
            System.out.println("Selling " + position.getCount() + " shares of " + position.getSecurity().getName() + " at " + position.getSecurity().getPrice() + " EUR Spot. Confirm (y/n)");
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return input.equals("y");
    }
}