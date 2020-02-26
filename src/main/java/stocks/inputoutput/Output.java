package stocks.inputoutput;

public class Output {

    public Help help = new Help();

    public void println(String string) {
        System.out.println(string);
    }

    public void println() {
        System.out.println();
    }

    public void print(String string) {
        System.out.print(string);
    }

    /**
     * Used to show help menus in console when the -help- command is executed
     */
    public static class Help {

        private static final String FORMAT = "%-15s %s%n";

        /**
         * Displays menu when no user is logged in
         */
        void noUser() {
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
        void loggedIn() {
            System.out.println();
            System.out.printf(FORMAT, "selected:", "Overview of user & portfolio currently selected");
            System.out.printf(FORMAT, "select:", "Select an existing portfolio");
            System.out.printf(FORMAT,"add:", "Add a new portfolio");
            System.out.printf(FORMAT, "lp:", "List all portfolios");
            System.out.printf(FORMAT, "lf:", "List all securities");
            System.out.printf(FORMAT, "ph:", "Select a security and show its price history");
            System.out.printf(FORMAT, "help:", "Shows this dialog");
            System.out.printf(FORMAT, "clear:", "Clears the console");
            System.out.printf(FORMAT, "logout:", "Logs current user out and displays the login menu");
            System.out.printf(FORMAT, "exit:", "Exit Stocks");
            System.out.println();
        }

        void portfolio() {
            System.out.println();
            System.out.printf(FORMAT, "selected:", "Overview of user & portfolio currently selected");
            System.out.printf(FORMAT, "select:", "Select an existing portfolio");
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
        void clear() {
            System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
        }
    }
}
