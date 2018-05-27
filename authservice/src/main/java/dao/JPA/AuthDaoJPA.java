package dao.JPA;

import dao.interfaces.AuthDao;
import domain.Credentials;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import java.util.HashMap;

@Stateless
@JPA
public class AuthDaoJPA implements AuthDao {
    HashMap<String, String> passwords = new HashMap<>();

    @PostConstruct
    public void init() {
        passwords.put("willem", "pass");
        passwords.put("joris", "pass");
        passwords.put("lorenzo", "pass");
        passwords.put("ken", "pass");
        passwords.put("mike", "pass");
        passwords.put("youri", "pass");
    }

    @Override
    public void Authenticate(Credentials cred) throws LoginException {
        String u = cred.getUsername();
        String p = cred.getPassword();

        if (passwords.containsKey(u)) {
            if (passwords.get(u).equals(p)) {
                System.out.println("Successfully loged in " + u);
                return;
            }

            abortLoginFailed("Login failed for user " + u);

        } else {
            abortLoginFailed("Could not find user " + u);
        }
    }

    private void abortLoginFailed(String message) throws LoginException {
        System.out.println(message);
        throw new LoginException(message);

    }
}
