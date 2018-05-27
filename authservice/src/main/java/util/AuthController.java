package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.ejb.Stateless;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Stateless
public class AuthController {
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final int JWT_DURATION_MIN = 30;

    private static Map<String, Object> rsaKeys = null;


    public AuthController() {

        try {
            if(rsaKeys == null) {
                System.out.println("generating keys");
                rsaKeys = getRSAKeys();
                System.out.println("generated keys");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return (PublicKey) rsaKeys.get("public");
    }

    public String generateJWT(String username) throws Exception {
        PublicKey publicKey = (PublicKey) rsaKeys.get("public");
        PrivateKey privateKey = (PrivateKey) rsaKeys.get("private");

        System.out.println("generating token");
        String token = generateToken(privateKey, username, JWT_DURATION_MIN);
        System.out.println("Generated Token:\n" + token);

        verifyToken(token, publicKey);

        return token;
    }


    private String generateToken(PrivateKey privateKey, String username, int duration) {
        System.out.println("GENERATING TOKEN FOR USER: " + username);
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date expirationDate = new Date(t + (duration * 60000));

        String token = null;

        try {
            token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.RS512, privateKey)
                    .compact();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private Claims verifyBearer(String bearer, PublicKey publicKey) throws Exception {
        String token = bearer.substring(AUTHENTICATION_SCHEME.length()).trim();
        return verifyToken(token, publicKey);
    }

    // verify and get claims using public key
    private Claims verifyToken(String token, PublicKey publicKey) throws Exception {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

            String subject = claims.getSubject();
            System.out.println("Successfully verified Token from subject " + subject);

        } catch (Exception e) {

            claims = null;
        }
        return claims;
    }

    // Get RSA keys. Uses key size of 2048.
    private Map<String, Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

//    private String generateToken(int duration, String subject, byte[] key) {
//        Calendar date = Calendar.getInstance();
//        long t = date.getTimeInMillis();
//        Date expirationDate = new Date(t + (duration * 60000));
//
//
//        String jwtToken = Jwts.builder()
//                .setSubject(subject)
//                .setIssuedAt(new Date())
//                .setExpiration(expirationDate)
//                .signWith(SignatureAlgorithm.HS256, key)
//                .compact();
//
//        return jwtToken;
//    }
//
//    public String getUserFromJWT(String bearer) throws Exception {
//        String token = bearer
//                .substring(AUTHENTICATION_SCHEME.length()).trim();
//
//        byte[] key = getKey();
//        return Jwts.parser()
//                .setSigningKey(key)
//                .parseClaimsJws(token).getBody().getSubject();
//
//    }
//
//    public byte[] getKey() throws Exception {
//        if (key == null) {
//            key = generateKey();
//        }
//
//        return key;
//    }
//
//    private byte[] generateKey() throws Exception {
//        return "secret".getBytes("UTF-8");
//    }


}
