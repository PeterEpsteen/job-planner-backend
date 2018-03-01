package api.models;

public class TokenResponse {
    private String token;

    public TokenResponse(){}

    public TokenResponse(String t) {
        token = t;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
