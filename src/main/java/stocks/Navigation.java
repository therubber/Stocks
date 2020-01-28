package stocks;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Navigation {

    Scanner scanner = new Scanner(System.in);

    public int navigation() {
        System.out.print("Enter: ");
        int input = readInt();
        switch (input) {
            default:
                return 8;
            case 1:
                System.out.print("Please enter your username: ");
                // TODO: Benutzer einloggen (login(read()));
                navigation();
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

}
