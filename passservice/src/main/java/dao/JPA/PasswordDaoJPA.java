package dao.JPA;

import dao.PasswordDAO;
import domain.Password;
import interceptor.Managed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Stateless
public class PasswordDaoJPA implements PasswordDAO {
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
    public List<Password> getPasswords(String user) throws NotFoundException {
        TypedQuery<Password> query = em.createNamedQuery("pass.findByUser", Password.class);
        query.setParameter("username", user);
        List<Password> result = query.getResultList();
        return result;
    }

    @Override
    public void setPassword(Password pass) {
        em.persist(pass);
    }

    @Override
    public void updatePassword(Password password) {
        em.persist(password);
    }

    @Override
    @Managed
    public void removePassword(Password password) {
        em.remove(password);
    }
}
