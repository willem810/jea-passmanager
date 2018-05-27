package dao;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.util.HashMap;

@Singleton
public class AuthDaoColl implements AuthDao {
    HashMap<String, String> jwts = new HashMap<>();


    @Override
    public String getJwtBearer(String user) {
        return jwts.get(user);
    }

    @Override
    public void setJwtBearer(String user, String jwt) {
        jwts.put(user, jwt);
    }

    @Override
    public void removeJwtBearer(String user) {
        jwts.remove(user);
    }
}
