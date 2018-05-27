package rest;

import interceptor.LogError;
import interceptor.Logging;
import interceptor.Secured;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author airhacks.com
 */
@Path("ping")
@Logging
@LogError
public class PingResource {

    @GET
    public String ping() {
        return "Enjoy Java EE 8!";
    }


    @GET
    @Path("secure")
    @Secured
    public String secured() {
        return "Enjoy Java EE 8!";
    }

}
