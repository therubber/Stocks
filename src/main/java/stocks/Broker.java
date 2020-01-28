package stocks;

import java.util.LinkedList;

public class Broker {

    LinkedList<User> users = new LinkedList<User>();
    LinkedList<Fund> funds = new LinkedList<Fund>();

    public static void main(String[] args) {
        Navigation navigation = new Navigation();
        int navCurrent = navigation.navigation();
        while (navCurrent != 0) {
            navCurrent = navigation.navigation();
        }
    }
}
