package stocks.inputoutput;

import java.util.InputMismatchException;
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
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
        }
        return input;
    }

    /**
     * Input method for double values
     */
    public static double doubleValue() {
        return scanner.nextDouble();
    }
}
