package rest.client;

import domain.Password;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Stateless
public class PasswordClient extends RestClient {

    Client client = ClientBuilder.newClient();


    private static String PASSSERVICE_BASE_URL = System.getenv("PASSSERVICE_BASE_URL");
    private static String PASSSERVICE_BASE_PORT = System.getenv("PASSSERVICE_BASE_PORT");
    private static String PASSSERVICE_GETPASSWORDS_REL = "/passmanager-passservice/resources/password/%s";
    private static String PASSSERVICE_GETPASSWORD_REL = "/passmanager-passservice/resources/password/%s/%s";
    private static String PASSSERVICE_SETPASSWORD_REL = "/passmanager-passservice/resources/password";
    private static String PASSSERVICE_REMOVEPASSWORD_REL = "/passmanager-passservice/resources/password/%s/%s/remove";
    private static String PASSSERVICE_UPDATEPASSWORD_REL = "/passmanager-passservice/resources/password/update";


    @PostConstruct
    public void init() {
        if (PASSSERVICE_BASE_URL == null) {
            PASSSERVICE_BASE_URL = "http://localhost";
        }
        if (PASSSERVICE_BASE_PORT == null) {
            PASSSERVICE_BASE_PORT = "8083";
        }
    }


    public void updatePassword(Password pass) throws WebApplicationException {
        String url = getUrl(PASSSERVICE_UPDATEPASSWORD_REL);
        postSecured(url, pass);
    }


    private String getUrl(String endpoint) {
        String url = PASSSERVICE_BASE_URL + ":" + PASSSERVICE_BASE_PORT + endpoint;


        if (url.contains("null")) {
            url = "http://localhost:8083/" + endpoint;
        }

        return url;
    }

}
