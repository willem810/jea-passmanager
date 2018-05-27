package rest;

import domain.Credentials;
import interceptor.LogError;
import interceptor.Logging;
import service.AuthService;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("auth")
@Logging
@LogError
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(Credentials credentials) {
        try {
            authService.login(credentials);
            return Response.ok().build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus(), e.getMessage()).build();
        } catch (EJBTransactionRolledbackException e) {
            Throwable origError = e.getCause().getCause();
            if (origError instanceof WebApplicationException) {
                return Response.status(((WebApplicationException) origError).getResponse().getStatus(), origError.getMessage()).build();
            } else {
                return Response.status(500, e.getMessage()).build();

            }
        }
    }

    @POST
    @Path("logout/{user}")
    public Response logout(@PathParam("user") String username) {
        authService.logout(username);
        return Response.ok().build();
    }


}
