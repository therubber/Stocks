package stocks.repo;

import stocks.dows.SecurityDow;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Securities {

    private Securities() {}

    private static List<SecurityDow> securityList = new LinkedList<>();

    public static SecurityDow get(SecurityDow securityDow) {
        return securityList.get(indexOf(securityDow));
    }

    public static SecurityDow get(String name) {
        return securityList.get(indexOf(new SecurityDow(name)));
    }

    public static SecurityDow get(int index) {
        return securityList.get(index);
    }

    public static List<SecurityDow> getAll() {
        return securityList;
    }

    public static boolean contains(SecurityDow security) {
        return securityList.contains(security);
    }

    public static int indexOf(SecurityDow securityDow) {
        return securityList.indexOf(securityDow);
    }

    public static boolean isEmpty() {
        return securityList.isEmpty();
    }

    public static void list() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-10s %-15s%n", "Name", "ISIN", "WKN", "Price", "Date");
        System.out.println();
        for (SecurityDow security : securityList) {
            System.out.printf("%-18s %-16s %-10s %-10.2f %-15s%n", security.getName(), security.getIsin(), security.getWkn(), security.getSpotPrice().getPrice(), security.getSpotDate());
        }
        System.out.println();
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
                if (!securityList.contains(new SecurityDow(name, isin, wkn))) {
                    securityList.add(new SecurityDow(name, isin, wkn));
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
        for (SecurityDow security : securityList) {
            security.update();
        }
    }

    public static boolean evaluateSpotPrices() {
        boolean validPrices = false;
        if (!isEmpty()) {
            updatePrices();
            validPrices = true;
        }
        return validPrices;
    }
}

