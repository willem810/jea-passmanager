package interceptor;


import com.sun.org.apache.xml.internal.security.Init;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.OkHttpClient;
import sun.misc.BASE64Decoder;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;

@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String AUTHSERVICE_BASE_URL = System.getenv("AUTHSERVICE_BASE_URL");
    private static final String AUTHSERVICE_BASE_PORT = System.getenv("AUTHSERVICE_BASE_PORT");
    private static final String AUTHSERVICE_PUBLICKEY_REL = System.getenv("AUTHSERVICE_PUBLICKEY_REL");


    private static final String REALM = "realm";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    Logger log = Logger.getLogger(this.getClass().getName());

    private static PublicKey publicKey;


    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        boolean securedMethod = false;
        Method method = resourceInfo.getResourceMethod();
        if (method != null && method.isAnnotationPresent(Secured.class)) {
            securedMethod = true;
        }

        boolean securedClass = false;
        Class<?> aClass = resourceInfo.getResourceClass();
        if (aClass != null && aClass.isAnnotationPresent(Secured.class)) {
            securedClass = true;
        }

        if (!securedMethod && !securedClass) {
            return;
        }

        try {
            init();
        } catch (Exception e) {
            abort(requestContext, 500, e.getMessage());
        }

        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }


        // Extract the token from the Authorization header
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        String user = getAuthenticationFor(requestContext);

        try {
            // Verify the token
            verifyToken(token, publicKey, user);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private void init() throws Exception {
        initKey();
    }

    private void initKey() throws Exception {
        if (publicKey == null) {

            String url = AUTHSERVICE_BASE_URL + ":" + AUTHSERVICE_BASE_PORT + AUTHSERVICE_PUBLICKEY_REL;

            if (url.contains("null")) {
                url = "http://localhost:8082/passmanager-authservice/resources/auth/publickey";
            }
            System.out.println("GETTING PUBLIC KEY FROM AUTH SERVICE: " + url);

            byte[] encodedKey;

            try {
                Client client = ClientBuilder.newClient();
                encodedKey = client.target(url)
                        .request()
                        .get(byte[].class);
            } catch (Exception e) {
                System.out.println("GETTING PUBLIC KEY FAILED! " + e.getMessage());
                throw e;
            }

            System.out.println("GETTING PUBLIC KEY SUCCESS! PROCESSING IT NOW... ");


            /*
            This has to be called, or else Base64 will throw an exception saying this should be called first
             */
            Init.init();

            KeyFactory factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encodedKey);
            PublicKey newKey = factory.generatePublic(encodedKeySpec);

            System.out.println("PROCESSING SUCCEEDED!");

            this.publicKey = newKey;
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .build());
    }

    private void abort(ContainerRequestContext requestContext, int status, String message) {
        requestContext.abortWith(
                Response.status(status, message).build());
    }


    private String getAuthenticationFor(ContainerRequestContext ctx) {

        MultivaluedMap<String, String> pathParam = ctx.getUriInfo().getPathParameters();
        return pathParam.getFirst("user");
    }

    // verify and get claims using public key
    private Claims verifyToken(String token, PublicKey publicKey, String user) throws Exception {
        Claims claims;
        claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

        String subject = claims.getSubject();

        if (user != null && user != "" && !subject.equals(user)) {
            throw new NotAuthorizedException("Calling request for user: " + subject + ", but token was meant for user: " + user);
        }

        System.out.println("Successfully verified Token from subject " + subject);
        return claims;
    }


}
