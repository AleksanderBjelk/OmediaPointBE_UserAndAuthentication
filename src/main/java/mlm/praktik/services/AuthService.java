package mlm.praktik.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final String GOOGLE_CLIENT_ID = "597932872393-f75kmuhqikket5k7kv31irvgr8ghh82j.apps.googleusercontent.com";

    private static final Key JWT_SECRET = Keys.hmacShaKeyFor("KCvxfSSBImPIqQrYOBvr1sGOHFeEfvMsQcqX3eh6fKM=".getBytes(StandardCharsets.UTF_8));

    public Map<String, String> validateToken(String idTokenString) {
        Map<String, String> response = new HashMap<>();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String sub = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");
                String aud = (String) payload.get("aud");
                logger.info("Token is valid. Token info: {}, {}, {}, {}", sub, email, name, picture);

                String jwt = Jwts.builder()
                        .setSubject(sub)
                        .claim("email", email)
                        .claim("name", name)
                        .claim("picture", picture)
                        .claim("aud", aud)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(JWT_SECRET, SignatureAlgorithm.HS256)
                        .compact();

                response.put("message", "Token verified");
                response.put("token", jwt);
                response.put("subject", sub);
                response.put("email", email);
                response.put("name", name);
                response.put("picture", picture);
                response.put("aud", aud);
            } else {
                logger.warn("Invalid ID token.");
                response.put("message", "Invalid ID token");
            }
        } catch (Exception e) {
            logger.error("Token validation error: ", e);
            response.put("message", "Token validation error: " + e.getMessage());
        }

        return response;
    }
}