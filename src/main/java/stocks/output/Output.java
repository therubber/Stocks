package stocks.output;

public class Output {

    private Output() {
    }

    static void help() {
        System.out.printf("%n%-15s %s%n", "login:", "Log in with existing user");
        System.out.printf("%-15s %s%n", "add:", "Register a new user");
        System.out.printf("%-15s %s%n", "lf", "Displays a list of all funds available in Stocks");
        System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
        System.out.printf("%-15s %s%n", "clear:", "Clears the console");
        System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
    }

    static void userHelp() {
        System.out.printf("%n%-15s %s%n", "selected:", "Overview of user & portfolio currently selected");
        System.out.printf("%-15s %s%n", "select:", "Select an existing portfolio");
        System.out.printf("%-15s %s%n","add:", "Add a new portfolio");
        System.out.printf("%-15s %s%n", "lp:", "List all portfolios");
        System.out.printf("%-15s %s%n", "lf:", "List all funds");
        System.out.printf("%-15s %s%n", "buy:", "Add a new position to the selected portfolio");
        System.out.printf("%-15s %s%n", "sell:", "Reduce an existing position in the selected portfolio");
        System.out.printf("%-15s %s%n", "ov:", "Overview of all positions in selected portfolio");
        System.out.printf("%-15s %s%n", "help:", "Shows this dialog");
        System.out.printf("%-15s %s%n", "clear:", "Clears the console");
        System.out.printf("%-15s %s%n", "logout:", "Logs current user out and displays the login menu");
        System.out.printf("%-15s %s%n%n", "exit:", "Exit Stocks");
    }

    static void clear() {
            System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
    }
}
