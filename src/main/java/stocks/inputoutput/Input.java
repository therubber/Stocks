package stocks.inputoutput;

import java.util.Scanner;

public class Input {

    // Private constructor to hide public one
    private Input() {}

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Checks validity of input
     * @return String input if valid
     */
    public static String stringValue() {
        return scanner.next();
    }

    /**
     * Checks validity of int input
     * @return Int input if valid
     */
    public static int intValue() {
        return scanner.nextInt();
    }

    /**
     * Input method for double values
     */
    public static double doubleValue() {
        return scanner.nextDouble();
    }
}
