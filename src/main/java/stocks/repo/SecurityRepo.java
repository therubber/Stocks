package stocks.repo;

import stocks.entities.Security;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SecurityRepo implements Iterable<Security>{

    private List<Security> securityList = new LinkedList<>();

    public void add(Security security) {
        securityList.add(security);
    }

    public void remove(Security security) {
        securityList.remove(security);
    }

    public Security get(Security securityDow) {
        return securityList.get(indexOf(securityDow));
    }

    public Security get(String name) {
        return securityList.get(indexOf(new Security(name)));
    }

    public Security get(int index) {
        return securityList.get(index);
    }

    public boolean contains(Security security) {
        return securityList.contains(security);
    }

    public int indexOf(Security securityDow) {
        return securityList.indexOf(securityDow);
    }

    public boolean isEmpty() {
        return securityList.isEmpty();
    }

    public void list() {
        System.out.println();
        System.out.printf("%-18s %-16s %-10s %-12s %-10s %-15s%n", "Name", "ISIN", "WKN", "Type", "Price", "Date");
        System.out.println();
        for (Security security : securityList) {
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
        for (Security security : securityList) {
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
            InputStream inputStream = Objects.requireNonNull(SecurityRepo.class.getResourceAsStream("/SecurityData/Securities.csv"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] fund = line.split(";");
                String name = fund[0];
                String isin = fund[1];
                String wkn = fund[2];
                String type = fund[3];
                if (!securityList.contains(new Security(name, isin, wkn, type))) {
                    securityList.add(new Security(name, isin, wkn, type));
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
        for (Security security : securityList) {
            security.update();
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
        return securityList.size();
    }

    /**
     * Clears the Repository
     */
    public void clear() {
        securityList.clear();
    }

    public Iterator<Security> iterator() {
        return securityList.iterator();
    }
}

