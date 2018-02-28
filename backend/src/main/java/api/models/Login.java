package api.models;

import sun.rmi.runtime.Log;

public class Login {
    private String username;
    private String password;

    public Login() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
