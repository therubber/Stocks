package stocks.utility;

import stocks.entities.Portfolio;
import stocks.entities.User;

public class NavigationContainer {

    private boolean exit = false;
    private User user;
    private Portfolio portfolio;

    public boolean getExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
