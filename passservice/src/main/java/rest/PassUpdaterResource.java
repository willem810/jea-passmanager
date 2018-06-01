package rest;

import interceptor.LogError;
import interceptor.Logging;
import util.PassUpdater;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("updater")
@Logging
@LogError
public class PassUpdaterResource {

    @Inject
    PassUpdater passUpdater;

    @POST
    public void update() {
        passUpdater.updatePasswords();
    }

    @POST
    @Path("start")
    public void start() {
        passUpdater.Start();
    }

    @POST
    @Path("stop")
    public void stop() {
        passUpdater.Stop();
    }


}
