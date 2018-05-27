package rest;

import interceptor.LogError;
import interceptor.Logging;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author airhacks.com
 */
@Path("ping")
@LogError
@Logging
public class PingResource {

    @GET
    public String ping() {
        return "Enjoy Java EE 8!";
    }

}
