package stocks;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Navigation {

    Scanner scanner = new Scanner(System.in);

    private void Navigation() {
        int input = readInt();
        switch (input) {
            case 1:
                System.out.println("Bitte geben sie ihren Benutzernamen ein: ");
                // TODO: Benutzer einloggen (login(read()));
                Navigation();
            case 2:
                // TODO: Benutzer ausloggen
                Navigation();
            case 3:
                // TODO: Depot ansteuern, übersicht anzeigen
            case 4:
                break;
        }
    }

    private int readInt() {
        return scanner.nextInt();
    }

}
