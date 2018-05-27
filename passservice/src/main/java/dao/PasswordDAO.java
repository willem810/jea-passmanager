package dao;

import domain.Password;

import javax.ws.rs.*;
import java.util.List;

public interface PasswordDAO {
    Password getPassword(String user, String service) throws NotFoundException;


    List<Password> getPasswords(String user) throws NotFoundException;


    void setPassword(Password pass);


    void updatePassword(Password password);

    void removePassword(Password password);
}
