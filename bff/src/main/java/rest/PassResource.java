package rest;

import dto.Password;
import interceptor.LogError;
import interceptor.Logging;
import service.PasswordService;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("password")
@Logging
@LogError
public class PassResource {
    @Inject
    PasswordService passwordService;

    @GET
    @Path("{user}")
    public Response getPasswords(@PathParam("user") String user) {
        try {
            return Response.ok(passwordService.getPasswords(user)).build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus(), e.getMessage()).build();
        } catch(EJBTransactionRolledbackException e){
            Throwable origError = e.getCause().getCause();
            if(origError instanceof WebApplicationException){
                return Response.status(((WebApplicationException) origError).getResponse().getStatus(), origError.getMessage()).build();
            } else {
                return Response.status(500, origError.getMessage()).build();

            }
        }
    }

    @GET
    @Path("{user}/{service}")
    public Response getPassword(@PathParam("user") String user, @PathParam("service") String service) {
        try {
            return Response.ok(passwordService.getPassword(user, service)).build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus(), e.getMessage()).build();
        } catch(EJBTransactionRolledbackException e){
            Throwable origError = e.getCause().getCause();
            if(origError instanceof WebApplicationException){
                return Response.status(((WebApplicationException) origError).getResponse().getStatus(), origError.getMessage()).build();
            } else {
                return Response.status(500, origError.getMessage()).build();

            }
        }
    }

    @POST
    @Path("")
    public Response setPassword(Password pass) {
        try {
            passwordService.setPassword(pass);
            return Response.ok().build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus(), e.getMessage()).build();
        } catch(EJBTransactionRolledbackException e){
            Throwable origError = e.getCause().getCause();
            if(origError instanceof WebApplicationException){
                return Response.status(((WebApplicationException) origError).getResponse().getStatus(), origError.getMessage()).build();
            } else {
                return Response.status(500, origError.getMessage()).build();

            }
        }
    }

    @POST
    @Path("{user}/{service}/remove")
    public Response removePassword(@PathParam("user") String user, @PathParam("service") String service) {
        try {
            passwordService.removePassword(user, service);
            return Response.ok().build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus(), e.getMessage()).build();
        } catch(EJBTransactionRolledbackException e){
            Throwable origError = e.getCause().getCause();
            if(origError instanceof WebApplicationException){
                return Response.status(((WebApplicationException) origError).getResponse().getStatus(), origError.getMessage()).build();
            } else {
                return Response.status(500, e.getMessage()).build();

            }
        }
    }
}
