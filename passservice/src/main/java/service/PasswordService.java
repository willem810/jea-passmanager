package service;

import dao.PasswordDAO;
import domain.Password;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Stateless
public class PasswordService {

    @Inject
    PasswordDAO passwordDAO;


    public Password getPassword(String user, String service) throws NotFoundException {
        Password pass = passwordDAO.getPassword(user, service);
        if(!pass.expired()){
            return pass;
        } else {

        }
    }

    public List<Password> getPasswords(String user) throws NotFoundException {
        return passwordDAO.getPasswords(user);
    }

    public void setPassword(Password pass) {
        passwordDAO.setPassword(pass);
    }

    public void updatePassword(Password password) {
        passwordDAO.updatePassword(password);
    }

    public void removePassword(Password password) {
        passwordDAO.removePassword(password);
    }

    public void removePassword(String user, String service) {
        Password pass = getPassword(user, service);
        removePassword(pass);
    }
}
