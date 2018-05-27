package rest;

import domain.Credentials;
import interceptor.LogError;
import interceptor.Logging;
import service.Authservice;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.security.PublicKey;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("auth")
@Stateless
@LogError
@Logging
public class AuthResource {

    @Inject
    Authservice authService;


    @GET
    public String ping() {
        return "You have found the auth resource....";
    }

    private Response abortWithError(int status, String message) {
        System.out.println(message);
        return Response.status(status, message).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {
        try {
            System.out.println("Authenticating user: " + credentials.getUsername());
            // Authenticate the user using the credentials provided
            authService.Authenticate(credentials);

            // Issue a token for the user
            String token = authService.GenerateToken(credentials.getUsername());
            System.out.println("Returning token: " + token);

            // Return the token on the response
            return Response.ok()
                    .header(AUTHORIZATION, "Bearer " + token)
                    .build();

        } catch (LoginException e) {
            return abortWithError(401,
                    "LOGIN FAILURE: " + e.getMessage());
        } catch (Exception e) {
            return abortWithError(500,
                    "ERROR WHILE AUTHENTICATING: " + e.getMessage());

        }
    }

    @GET
    @Path("/publickey")
    @Produces(MediaType.TEXT_PLAIN)
    public Response GetPublicKey() {
        // Authenticate the user using the credentials provided
        PublicKey key = authService.GetPublicKey();

        // Return the token on the response
        return Response.ok(key.getEncoded()).build();
    }
}
