package stocks.repo;

import stocks.factories.SecurityFactory;
import stocks.inputoutput.Input;
import stocks.inputoutput.Output;
import stocks.entities.Security;
import java.io.*;
import java.util.*;

public class SecurityRepo implements Iterable<Security>{

    private List<Security> securities = new LinkedList<>();
    private final SecurityFactory factory = new SecurityFactory();
    private final Input input = new Input();
    private final Output out = new Output();

    public void add(Security security) {
        securities.add(security);
    }

    public void remove(Security security) {
        securities.remove(security);
    }

    public Security get(Security securityDow) {
        return securities.get(indexOf(securityDow));
    }

    public Security get(String name) {
        return securities.get(indexOf(factory.createSecurity(name)));
    }

    public Security get(int index) {
        return securities.get(index);
    }

    public boolean contains(Security security) {
        return securities.contains(security);
    }

    public int indexOf(Security securityDow) {
        return securities.indexOf(securityDow);
    }

    public boolean isEmpty() {
        return securities.isEmpty();
    }

    public void listSecurities() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-12s %-10s %-15s%n", "Name", "ISIN", "WKN", "Type", "Price", "Date");
        System.out.println();
        for (Security security : securities) {
            System.out.printf("%-18s %-16s %-10s %-12s %-10.2f %-15s%n", security.getName(), security.getIsin(), security.getWkn(), security.getType(), security.getSpotPrice().getPrice(), security.getSpotDate());
        }
        System.out.println();
    }


    /**
     * Displays an indexed list of all securities available. Index begins at 1 for simplicity of use
     */
    public void listIndexed() {
        System.out.println();
        System.out.printf("%5s %-18s %-16s %-10s %-16s %-10s %-15s%n", "Index", "Name", "ISIN", "WKN", "Type", "Price", "Date");
        int index = 1;
        for (Security security : securities) {
            System.out.printf("%-5d %-18s %-16s %-10s %-16s %-10.2f %-15s%n", index, security.getName(), security.getIsin(), security.getWkn(), security.getType(), security.getSpotPrice().getPrice(), security.getSpotDate());
            index++;
        }
        System.out.println();
    }

    /**
     * Method to load Securities and their prices
     */
    public void load() {
        initiate();
        updatePrices();
    }

    /**
     * Sets up names of available security and fetches data using updateSecurities()
     */
    public void initiate() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/SecurityData/Securities.csv")));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] fund = line.split(";");
                String name = fund[0];
                String isin = fund[1];
                String wkn = fund[2];
                String type = fund[3];
                if (!securities.contains(factory.createSecurity(name, isin, wkn, type))) {
                    securities.add(factory.createSecurity(name, isin, wkn, type));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates prices to most recent using data from SecurityData file and fills price history with all other found historical prices
     */
    public void updatePrices() {
        for (Security security : securities) {
            security.update();
        }
    }

    public void priceHistory() {
        listIndexed();
        out.println("Enter the index of the Security whose price history you want to display or 0 to exit:");
        try {
            int index = input.intValue();
            if (index == 0) {
                out.println("Going back to main menu...");
            } else if (index <= size()) {
                Security selectedSecurity = get(index - 1);
                selectedSecurity.priceHistory();
            } else {
                out.println("Index invalid. Please try again.");
                priceHistory();
            }
        } catch (InputMismatchException e) {
            out.println("Index invalid. Please try again.");
        }
    }

    /**
     * Checks if the spot prices in /SpotData/ are valid
     * @return Boolean whether the price data in SpotData is valid
     */
    public boolean evaluateSpotPrices() {
        boolean validPrices = false;
        if (!isEmpty()) {
            updatePrices();
            validPrices = true;
        }
        return validPrices;
    }

    /**
     * Gets the amount of security objects contained in securityList
     * @return Int amount of security objects contained
     */
    public int size() {
        return securities.size();
    }

    /**
     * Clears the Repository
     */
    public void clear() {
        securities.clear();
    }

    public Iterator<Security> iterator() {
        return securities.iterator();
    }
}

