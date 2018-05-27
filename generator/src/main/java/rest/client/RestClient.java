package rest.client;

import interceptor.LogError;
import interceptor.Logging;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@LogError
@Logging
public class RestClient {
    Client client = ClientBuilder.newClient();
    private final String AUTHORIZATION_HEADER = "Authorization";

    public <T> T get(String url, Class<T> aClass, MultivaluedMap<String, Object> headers) throws WebApplicationException {
        Response resp = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .get(Response.class);

        if (resp.getStatus() != 200) {
            throw new WebApplicationException(resp);
        }

        return resp.readEntity(aClass);
    }

    public Response post(String url, Object body, MultivaluedMap<String, Object> headers) throws WebApplicationException {
        Response resp = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(body, MediaType.APPLICATION_JSON));

        if (resp.getStatus() != 200) {
            throw new WebApplicationException(resp);
        }

        return resp;
    }

    public <T> T get(String url, Class<T> aClass) throws WebApplicationException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        return get(url, aClass, headers);
    }

    public Response get(String url) throws WebApplicationException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        return get(url, Response.class, headers);
    }

    public Response post(String url, Object body) throws WebApplicationException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        return post(url, body, headers);

    }

    public Response post(String url) throws WebApplicationException {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        return post(url, null, headers);
    }

//    public <T> T getSecured(String user, String url, Class<T> aClass) throws WebApplicationException {
//        List<Object> authBearer = new ArrayList<>();
//        authBearer.add(authService.getJwtBearer(user));
//
//        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
//        headers.put(AUTHORIZATION_HEADER, authBearer);
//
//        return get(url, aClass, headers);
//    }
//
//    public Response getSecured(String user, String url) throws WebApplicationException {
//        return getSecured(user, url, Response.class);
//    }
//
//    public Response postSecured(String user, String url, Object body) throws WebApplicationException {
//        List<Object> authBearer = new ArrayList<>();
//        authBearer.add(authService.getJwtBearer(user));
//
//        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
//        headers.put(AUTHORIZATION_HEADER, authBearer);
//
//        return post(url, body, headers);
//    }
//
//    public Response postSecured(String user, String url) throws WebApplicationException {
//        return postSecured(user, url, null);
//    }
}
