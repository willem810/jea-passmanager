package dao;

import domain.Password;

import javax.ws.rs.*;
import java.util.List;

public interface PasswordDAO {
    Password getPassword(String user, String service) throws NotFoundException;

    Password getPassword(long id) throws NotFoundException;


    List<Password> getPasswords(String user) throws NotFoundException;

    List<Password> getAllPasswords();


    void setPassword(Password pass);


    void updatePassword(Password password);

    void removePassword(Password password);
}
