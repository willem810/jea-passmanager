package dao.interfaces;

import domain.Credentials;

import javax.security.auth.login.LoginException;

public interface AuthDao {
    public void Authenticate(Credentials cred) throws LoginException;
}
