package service;

import dao.JPA.JPA;
import dao.interfaces.AuthDao;
import domain.Credentials;
import util.AuthController;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.security.PublicKey;

@Stateless
public class Authservice {

    @Inject
    @JPA
    AuthDao authDao;


    @Inject
    AuthController authController;

    public void Authenticate(Credentials cred) throws LoginException {
        authDao.Authenticate(cred);
    }

    public String GenerateToken(String username) throws Exception {
        return authController.generateJWT(username);

    }

    public PublicKey GetPublicKey() {
        return authController.getPublicKey();
    }
}
