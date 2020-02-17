package stocks.repo;

import stocks.entities.Security;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Securities {

    private Securities() {}

    private static List<Security> securityList = new LinkedList<>();

    public static Security get(Security securityDow) {
        return securityList.get(indexOf(securityDow));
    }

    public static Security get(String name) {
        return securityList.get(indexOf(new Security(name)));
    }

    public static Security get(int index) {
        return securityList.get(index);
    }

    public static List<Security> getAll() {
        return securityList;
    }

    public static boolean contains(Security security) {
        return securityList.contains(security);
    }

    public static int indexOf(Security securityDow) {
        return securityList.indexOf(securityDow);
    }

    public static boolean isEmpty() {
        return securityList.isEmpty();
    }

    public static void list() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-10s %-15s%n", "Name", "ISIN", "WKN", "Price", "Date");
        System.out.println();
        for (Security security : securityList) {
            System.out.printf("%-18s %-16s %-10s %-10.2f %-15s%n", security.getName(), security.getIsin(), security.getWkn(), security.getSpotPrice().getPrice(), security.getSpotDate());
        }
        System.out.println();
    }


    /**
     * Displays an indexed list of all securities available. Index begins at 1 for simplicity of use
     * @return Int count of securities available
     */
    public static int listIndexed() {
        System.out.println();
        System.out.printf("%5s %-18s %-16s %-10s %-10s %-15s%n", "Index", "Name", "ISIN", "WKN", "Price", "Date");
        int index = 1;
        for (Security security : securityList) {
            System.out.printf("%-5d %-18s %-16s %-10s %-10.2f %-15s%n", index, security.getName(), security.getIsin(), security.getWkn(), security.getSpotPrice().getPrice(), security.getSpotDate());
            index++;
        }
        System.out.println();
        return index;
    }

    /**
     * Method to load Securities and their prices
     */
    public static void load() {
        initiate();
        updatePrices();
    }

    /**
     * Sets up names of available security and fetches data using updateSecurities()
     */
    public static void initiate() {
        try {
            BufferedReader input = new BufferedReader(new FileReader("SecurityData/Securities.csv"));
            String line;
            while ((line = input.readLine()) != null) {
                String[] fund = line.split(";");
                String name = fund[0];
                String isin = fund[1];
                String wkn = fund[2];
                if (!securityList.contains(new Security(name, isin, wkn))) {
                    securityList.add(new Security(name, isin, wkn));
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: File Securities.csv not found. Unable to load securities.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates prices to most recent using data from SecurityData file and fills price history with all other found historical prices
     */
    public static void updatePrices() {
        for (Security security : securityList) {
            security.update();
        }
    }

    /**
     * Checks if the spot prices in /SpotData/ are valid
     * @return Boolean whether the price data in SpotData is valid
     */
    public static boolean evaluateSpotPrices() {
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
    public static int size() {
        return securityList.size();
    }
}

