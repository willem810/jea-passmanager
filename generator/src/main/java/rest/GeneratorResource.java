package rest;

import service.GeneratorService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("PasswordGenerator")
public class GeneratorResource {

    @Inject
    GeneratorService generatorService;


    @GET
    public Response generate() {
        return Response.ok(generatorService.generatePassword()).build();
    }

    @GET
    @Path("/{length}")
    public Response generate(@PathParam("length") int length) {
        return Response.ok(generatorService.generatePassword(length)).build();
    }
}
