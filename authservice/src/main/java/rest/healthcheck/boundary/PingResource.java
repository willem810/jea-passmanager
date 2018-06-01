package rest.healthcheck.boundary;


import rest.healthcheck.control.JsonCollectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Path("ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        return Response.ok("AVAILABLE").build();
    }

    @GET
    @Path("/echo/{echo}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response echo(@PathParam("echo") String param) {
        return Response.ok(param).build();
    }

    @GET
    @Path("/system-properties")
    public Response systemProperties() {
        Properties properties = System.getProperties();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        JsonObject obj = entries.stream().collect(JsonCollectors.toJsonBuilder()).build();
        return Response.ok(obj).build();
    }

    @GET
    @Path("/environment-variables")
    public Response environmentVariables() {
        Map<String, String> environment = System.getenv();
        JsonObject obj = environment.entrySet().stream().collect(JsonCollectors.toJsonBuilder()).build();
        return Response.ok(obj).build();
    }

    @GET
    @Path("/jndi/{namespace}")
    public Response jndi(@PathParam("namespace") String namespace) throws NamingException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        InitialContext c = new InitialContext();
        NamingEnumeration<NameClassPair> list = c.list(namespace);
        while (list.hasMoreElements()) {
            NameClassPair nameClassPair = list.nextElement();
            String name = nameClassPair.getName();
            String type = nameClassPair.getClassName();
            builder.add(name, type);
        }
        JsonObject obj = builder.build();
        return Response.ok(obj).build();
    }
}
