package rest.client;

import dto.Password;
import enums.ReadyStatus;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Stateless
public class PasswordClient extends RestClient {

    Client client = ClientBuilder.newClient();


    private static String PASSSERVICE_BASE_URL = System.getenv("PASSSERVICE_BASE_URL");
    private static String PASSSERVICE_BASE_PORT = System.getenv("PASSSERVICE_BASE_PORT");
    private static String PASSSERVICE_GETPASSWORDS_REL = "/passmanager-passservice/resources/password/%s";
    private static String PASSSERVICE_GETPASSWORD_REL = "/passmanager-passservice/resources/password/%s/%s";
    private static String PASSSERVICE_SETPASSWORD_REL = "/passmanager-passservice/resources/password";
    private static String PASSSERVICE_REMOVEPASSWORD_REL = "/passmanager-passservice/resources/password/%s/%s/remove";
    private static String PASSSERVICE_STATUS = "/passmanager-passservice/resources/health";


    @PostConstruct
    public void init() {
        if (PASSSERVICE_BASE_URL == null) {
            PASSSERVICE_BASE_URL = "http://localhost";
        }
        if (PASSSERVICE_BASE_PORT == null) {
            PASSSERVICE_BASE_PORT = "8083";
        }
    }

    public Password getPassword(String user, String service) throws WebApplicationException {
        String url = getUrl(PASSSERVICE_GETPASSWORD_REL);
        url = String.format(url, user, service);

        Password password = getSecured(user, url, Password.class);

        return password;
    }

    public List<Password> getPasswords(String user) throws WebApplicationException {

        String url = getUrl(PASSSERVICE_GETPASSWORDS_REL);

        url = String.format(url, user);
        Password[] passwords = getSecured(user, url, Password[].class);
        return Arrays.asList(passwords);
    }

    public void setPassword(Password pass) throws WebApplicationException {
        String url = getUrl(PASSSERVICE_SETPASSWORD_REL);
        postSecured(pass.getUsername(), url, pass);
    }

    public void removePassword(String user, String service) throws WebApplicationException {
        String url = getUrl(PASSSERVICE_REMOVEPASSWORD_REL);
        url = String.format(url, user, service);
        postSecured(service, url);
    }

    private String getUrl(String endpoint) {
        String url = PASSSERVICE_BASE_URL + ":" + PASSSERVICE_BASE_PORT + endpoint;


        if (url.contains("null")) {
            url = "http://localhost:8083/" + endpoint;
        }

        return url;
    }

    @Override
    public ReadyStatus getStatus() {
        try {
            JsonObject obj = get(getUrl(PASSSERVICE_STATUS), JsonObject.class);
            String systemStatus = obj.getString("System");
            ReadyStatus status = Enum.valueOf(ReadyStatus.class, systemStatus);
            setStatus(status);
            return status;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            setStatus(ReadyStatus.UNAVAILABLE);

            return ReadyStatus.UNAVAILABLE;
        }
    }
}
