package dao;

public interface AuthDao {

    String getJwtBearer(String user);

    void setJwtBearer(String user, String jwt);

    void removeJwtBearer(String user);
}
