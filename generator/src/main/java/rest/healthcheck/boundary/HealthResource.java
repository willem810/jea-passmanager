package rest.healthcheck.boundary;


import enums.ReadyStatus;
import rest.healthcheck.control.ServerWatch;
import utils.HealthCheck;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @Inject
    ServerWatch watch;

    @GET
    public Response status() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("System", ReadyStatus.AVAILABLE.toString());

        // Do this to prevent java.util.ConcurrentModificationException
        ArrayList<HealthCheck> checks = new ArrayList<>(HealthCheck.checks);
        for (HealthCheck check : checks) {
            String status = ReadyStatus.NOT_STARTED.toString();
            try {
                status = check.getStatus().toString();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            builder.add(check.getClass().getName(), status);
        }

        return Response.ok(builder.build()).build();
    }

    @GET
    @Path("/start-time")
    @Produces(MediaType.TEXT_PLAIN)
    public String bootTime() {
        return this.watch.getDateTime().toString();
    }

    @GET
    @Path("/current-memory")
    public JsonObject availableHeap() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Total memory in mb", this.watch.totalMemoryInMB()).
                add("Available memory in mb", this.watch.availableMemoryInMB()).
                add("Used memory in mb", this.watch.usedMemoryInMb()).
                add("Memory at start time", this.watch.usedMemoryInMbAtStartTime());
        return builder.build();
    }

    @GET
    @Path("/os-info")
    public JsonObject osInfo() {
        return this.watch.osInfo();
    }
}
