package rest.client;

import domain.Credentials;
import dto.Password;
import enums.ReadyStatus;
import utils.HealthCheck;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

@Stateless
public class AuthClient extends RestClient {

    private static String AUTHSERVICE_BASE_URL = System.getenv("AUTHSERVICE_BASE_URL");
    private static String AUTHSERVICE_BASE_PORT = System.getenv("AUTHSERVICE_BASE_PORT");
    private static final String AUTHSERVICE_LOGIN_REL = "/passmanager-authservice/resources/auth/login";
    private static final String AUTHSERVICE_STATUS = "/passmanager-authservice/resources/health";
    private final String AUTHORIZATION_HEADER = "Authorization";

    @PostConstruct
    public void init() {
        if (AUTHSERVICE_BASE_URL == null) {
            AUTHSERVICE_BASE_URL = "http://localhost";
        }
        if (AUTHSERVICE_BASE_PORT == null) {
            AUTHSERVICE_BASE_PORT = "8082";
        }


    }

    public String login(Credentials credentials) throws WebApplicationException {
        String url = getUrl(AUTHSERVICE_LOGIN_REL);

        String jwtBearer = post(url, credentials)
                .getHeaderString(AUTHORIZATION_HEADER);

        return jwtBearer;
    }

    private String getUrl(String endpoint) {
        String url = AUTHSERVICE_BASE_URL + ":" + AUTHSERVICE_BASE_PORT + endpoint;


        if (url.contains("null")) {
            url = "http://localhost:8081/" + endpoint;
        }

        return url;
    }

    @Override
    public ReadyStatus getStatus() {
        try {
            JsonObject obj = get(getUrl(AUTHSERVICE_STATUS), JsonObject.class);
            String systemStatus = obj.getString("System");
            ReadyStatus status = Enum.valueOf(ReadyStatus.class, systemStatus);
            setStatus(status);
            return status;
        } catch (Exception e) {
            setStatus(ReadyStatus.UNAVAILABLE);

            return ReadyStatus.UNAVAILABLE;
        }
    }
}
