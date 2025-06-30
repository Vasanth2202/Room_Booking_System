package Models;

public class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getusername() {
        return username;
    }

    public String getpassword() {
        return password;
    }

    public String getrole() {
        return role;
    }
}
