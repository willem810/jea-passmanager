package rest;

import domain.Password;
import dto.PasswordDTO;
import interceptor.LogError;
import interceptor.Logging;
import interceptor.Secured;
import service.PasswordService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("password")
@Logging
@LogError
public class PasswordResource {

    @Inject
    PasswordService passwordService;

    @GET
    @Path("{user}")
    @Secured
    public Response getPasswords(@PathParam("user") String user) {
        try {
            return Response.ok(passwordService.getPasswords(user)).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }

    @GET
    @Path("{user}/{service}")
    @Secured
    public Response getPassword(@PathParam("user") String user, @PathParam("service") String service) {
        try {
            return Response.ok(passwordService.getPassword(user, service)).build();
        } catch (NotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }

    @POST
    @Path("")
    @Secured
    public Response setPassword(Password pass) {
        try {
            passwordService.setPassword(pass);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }

    @POST
    @Path("{user}/{service}/remove")
    @Secured
    public Response removePassword(@PathParam("user") String user, @PathParam("service") String service) {
        try {
            passwordService.removePassword(user, service);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }


}
