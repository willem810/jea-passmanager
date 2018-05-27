package service;

import dao.AuthDao;
import domain.Credentials;
import rest.client.AuthClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;

@Stateless
public class AuthService {

    @Inject
    AuthDao authDao;

    @Inject
    AuthClient authClient;

    public String getJwtBearer(String user) {
        return authDao.getJwtBearer(user);
    }

    public void setJwtBearer(String user, String jwt) {
        authDao.setJwtBearer(user, jwt);
    }

    public void login(Credentials credentials) throws WebApplicationException {
        String jwtBearer = authClient.login(credentials);
        authDao.setJwtBearer(credentials.getUsername(), jwtBearer);
    }

    public void logout(String username) {
        authDao.removeJwtBearer(username);
    }

}
