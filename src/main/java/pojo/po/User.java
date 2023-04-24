package pojo.po;

/**
 * @author Secret
 */
public class User {
    String username;
    String password;
    int merit;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.merit = 0;
    }

    public User(String username, String password, int merit) {
        this.username = username;
        this.password = password;
        this.merit = merit;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public int getMerit() {
        return merit;
    }

    public void setMerit(int merit) {
        this.merit = merit;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
