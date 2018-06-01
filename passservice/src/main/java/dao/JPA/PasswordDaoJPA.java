package dao.JPA;

import dao.PasswordDAO;
import domain.Password;
import interceptor.Managed;
import service.PasswordService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Stateless
public class PasswordDaoJPA implements PasswordDAO {
    @Inject
    PasswordService passwordService;

    @PersistenceContext(unitName = "PassManagerPU")
    private EntityManager em;

    @Override
    public Password getPassword(String user, String service) throws NotFoundException {
        TypedQuery<Password> query = em.createNamedQuery("pass.findByUserAndService", Password.class);
        query.setParameter("username", user);
        query.setParameter("service", service);
        List<Password> result = query.getResultList();
        return result.get(0);
    }

    @Override
    public Password getPassword(long id) throws NotFoundException {
        TypedQuery<Password> query = em.createNamedQuery("pass.findById", Password.class);
        query.setParameter("id", id);
        List<Password> result = query.getResultList();
        return result.get(0);
    }

    @Override
    public List<Password> getPasswords(String user) throws NotFoundException {
        TypedQuery<Password> query = em.createNamedQuery("pass.findByUser", Password.class);
        query.setParameter("username", user);
        List<Password> result = query.getResultList();
        return result;
    }

    @Override
    public List<Password> getAllPasswords() {
        Query query = em.createQuery("SELECT p FROM Password p");
        return query.getResultList();
    }

    @Override
    public void setPassword(Password pass) {
        em.persist(pass);
    }

    @Override
    public void updatePassword(Password password) {
        getPassword(password.getUsername(), password.getService()).setPassword(password.getPassword());
    }

    @Override
    @Managed
    public void removePassword(Password password) {
        em.remove(password);
    }
}
