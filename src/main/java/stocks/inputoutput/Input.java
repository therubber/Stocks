package stocks.inputoutput;

import java.util.Scanner;

public class Input {

    private InputFromConsole inputFromConsole = new InputFromConsole();

    /**
     * Checks validity of input
     * @return String input if valid
     */
    public String stringValue() {
        return inputFromConsole.stringValue();
    }

    /**
     * Checks validity of int input
     * @return Int input if valid
     */
    public int intValue() {
        return inputFromConsole.intValue();
    }

    /**
     * Input method for double values
     */
    public double doubleValue() {
        return inputFromConsole.doubleValue();
    }

    /**
     * Handles the input from console using a Scanner
     */
    private static class InputFromConsole {

        private final Scanner scanner = new Scanner(System.in);

        /**
         * Checks validity of input
         * @return String input if valid
         */
        public String stringValue() {
            return scanner.next();
        }

        /**
         * Checks validity of int input
         * @return Int input if valid
         */
        public int intValue() {
            return scanner.nextInt();
        }

        /**
         * Input method for double values
         */
        public double doubleValue() {
            return scanner.nextDouble();
        }
    }
}
