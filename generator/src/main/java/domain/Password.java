package domain;

public class Password {
    long id;
    String password;
    String service;
    String username;

    public Password(long id, String password, String service, String username) {
        this.id = id;
        this.password = password;
        this.service = service;
        this.username = username;
    }

    public Password(String password, String service, String username) {
        this.password = password;
        this.service = service;
        this.username = username;
    }

    public Password() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
