package service;

import dto.Password;
import rest.client.PasswordClient;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.ejb.TransactionRolledbackLocalException;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
public class PasswordService {

    @Inject
    PasswordClient passwordClient;


    public Password getPassword(String user, String service) throws WebApplicationException {
        // return passwordDAO.getPassword(user, service);
        return passwordClient.getPassword(user, service);
    }

    public List<Password> getPasswords(String user) throws WebApplicationException {
        return passwordClient.getPasswords(user);

    }

    public void setPassword(Password pass) throws WebApplicationException {
        passwordClient.setPassword(pass);
    }


    public void removePassword(Password password) throws WebApplicationException {
        removePassword(password.getUsername(), password.getService());
    }

    public void removePassword(String user, String service) throws WebApplicationException {
        passwordClient.removePassword(user, service);
    }
}
