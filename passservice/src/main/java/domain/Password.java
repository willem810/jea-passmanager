package domain;

import javax.persistence.*;
import javax.security.auth.login.CredentialExpiredException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@NamedQueries({
        @NamedQuery(name = "pass.findByUser", query = "SELECT p FROM Password p WHERE p.username = :username"),
        @NamedQuery(name = "pass.findById", query = "SELECT p FROM Password p WHERE p.id = :id"),
        @NamedQuery(name = "pass.findByUserAndService", query = "SELECT p FROM Password p WHERE p.username = :username AND p.service = :service")
})
public class Password {

    private final long timeValid = TimeUnit.MILLISECONDS.toMillis(30);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String password;
    String service;
    String username;
    long date;

    public Password(String password, String service, String username) {
        this.password = password;
        this.service = service;
        this.username = username;
        date = System.currentTimeMillis();
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean expired() {
        long now = System.currentTimeMillis();
        return (now - date) >= timeValid;
    }


    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", id, username, service, password);
    }
}

