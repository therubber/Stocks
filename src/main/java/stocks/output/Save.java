package stocks.output;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import stocks.dows.SpotPrice;
import stocks.entities.Portfolio;
import stocks.entities.Position;
import stocks.entities.User;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Save {

    private Save() {}

    public static void toJson(Navigation currentInstance) {

        JSONArray userListJson = new JSONArray();
        for (User user : currentInstance.getUsers()) {
            JSONObject userJson = new JSONObject();
            JSONArray userPortfoliosJson = new JSONArray();
            userJson.put("username", user.getUsername());
            userJson.put("equity", user.getEquity());
            List<Portfolio> userPortfolios = user.getPortfolios();
            for (Portfolio portfolio : userPortfolios) {
                JSONObject portfolioJson = new JSONObject();
                portfolioJson.put("name", portfolio.getName());
                portfolioJson.put("equity", portfolio.getEquity());
                portfolioJson.put("owner", user);
                JSONArray positionsJson = new JSONArray();
                for (Position position : portfolio.getPositions()) {
                    JSONObject positionJson = new JSONObject();
                    JSONObject securityJson = new JSONObject();
                    JSONArray historicalPricesJson = new JSONArray();
                    securityJson.put("name", position.getSecurity().getName());
                    securityJson.put("isin", position.getSecurity().getIsin());
                    securityJson.put("wkn", position.getSecurity().getWkn());
                    for (SpotPrice price : position.getSecurity().getHistoricalPrices()) {
                        JSONObject histPriceJson = new JSONObject();
                        histPriceJson.put("price", price.getPrice());
                        histPriceJson.put("date", price.getDate());
                        historicalPricesJson.add(histPriceJson);
                    }
                    securityJson.put("historicalPrices", historicalPricesJson);
                    positionJson.put("count", position.getCount());
                    positionJson.put("id", position.getId());
                    positionJson.put("executionDate", position.getExecutionDate());
                    positionJson.put("security", position.getSecurity());
                    positionsJson.add(positionJson);
                }
                portfolioJson.put("positions", positionsJson);
                userPortfoliosJson.add(portfolioJson);
            }
            userJson.put("portfolios", userPortfoliosJson);
            userListJson.add(userJson);
        }
        try (FileWriter file = new FileWriter("users.json")) {
            file.write(userListJson.toJSONString());
            file.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void toXml(Navigation currentInstance) {
        try {
            final XMLEncoder encoder = new XMLEncoder(new ObjectOutputStream(new FileOutputStream("Saves/recent.xml")));
            encoder.writeObject(currentInstance.getUsers());
            encoder.close();
        } catch (IOException ioe) {
            System.out.println("Savefile not found, saving failed!");
        }
    }
}
