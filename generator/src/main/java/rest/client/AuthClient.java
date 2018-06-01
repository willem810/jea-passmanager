package rest.client;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.WebApplicationException;

@Stateless
public class AuthClient extends RestClient {

    private static String AUTHSERVICE_BASE_URL = System.getenv("AUTHSERVICE_BASE_URL");
    private static String AUTHSERVICE_BASE_PORT = System.getenv("AUTHSERVICE_BASE_PORT");
    private static final String AUTHSERVICE_LOGIN_REL = "/passmanager-authservice/resources/auth/login";
    private final String AUTHORIZATION_HEADER = "Authorization";

    private static final String USERNAME = "passUpdater";
    private static final String PASSWORD = "pass";

    private String jwtBearer;

    @PostConstruct
    public void init() {
        if (AUTHSERVICE_BASE_URL == null) {
            AUTHSERVICE_BASE_URL = "http://localhost";
        }
        if (AUTHSERVICE_BASE_PORT == null) {
            AUTHSERVICE_BASE_PORT = "8082";
        }

        this.jwtBearer = login();
    }

    public String login() throws WebApplicationException {
        String url = getUrl(AUTHSERVICE_LOGIN_REL);
        JsonObjectBuilder obj = Json.createObjectBuilder()
                .add("username", USERNAME)
                .add("password", PASSWORD);

        String jwtBearer = post(url, obj.build())
                .getHeaderString(AUTHORIZATION_HEADER);

        return jwtBearer;
    }

    public String getJwtBearer() {
        return jwtBearer;
    }

    private String getUrl(String endpoint) {
        String url = AUTHSERVICE_BASE_URL + ":" + AUTHSERVICE_BASE_PORT + endpoint;


        if (url.contains("null")) {
            url = "http://localhost:8082/" + endpoint;
        }

        return url;
    }
}
