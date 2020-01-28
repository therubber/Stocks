package stocks;

public class User {
    public int key;
    public String username;
    private double equity;
    private String equityDenominator;

    public User(String username) {
        this.equity = 10000.0;
        this.username = username;
        System.out.println("New user successfully created!");
    }
}
