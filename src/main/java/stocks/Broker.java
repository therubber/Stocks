package stocks;

import stocks.funds.Fund;

import java.util.LinkedList;
import java.util.Scanner;

public class Broker {

    private User selectedUser;
    private Depot selectedDepot;
    private Scanner scanner = new Scanner(System.in);
    private LinkedList<User> users = new LinkedList<User>();
    private LinkedList<Fund> funds = new LinkedList<Fund>();

    public static void main(String[] args) {
        Broker broker = new Broker();
        int navCurrent = broker.navigation();
        while (navCurrent != 0) {
            navCurrent = broker.navigation();
        }
    }


    public int navigation() {
        System.out.print("Enter: ");
        int input = readInt();
        switch (input) {
            default:
                return 8;
            case 1:
                System.out.print("Please enter your username: ");
                String username = scanner.nextLine();
                login(username);
                return 1;
            case 2:
                System.out.println("Enter a username to create a new user: ");
                // TODO: Benutzer anlegen
                navigation();
                return 2;
            case 3:
                // TODO: Depot ansteuern, übersicht anzeigen
                return 3;
            case 4:
                // TODO: Save current state of all users and depots
                return 0;
            case 8:
                help();
        }
        return 0;
    }

    private void help() {
        System.out.println("1: Log in with existing user");
        System.out.println("2: Register a new user");
        System.out.println("3: ");
    }

    private int readInt() {
        return scanner.nextInt();
    }

    private void login(String username) {
        if (users.contains(new User(username))) {
            selectedUser = users.get(users.indexOf(new User(username)));
        } else {
            System.out.println("User does not exist! Please register a new User");
        }
    }
}
