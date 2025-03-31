package mlm.praktik.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final String GOOGLE_CLIENT_ID = "597932872393-f75kmuhqikket5k7kv31irvgr8ghh82j.apps.googleusercontent.com";
    private final JwtTokenGenerator jwtTokenGenerator;
    private final GoogleIdTokenVerifier verifier;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthService(JwtTokenGenerator jwtTokenGenerator, UserService userService, UserRepository userRepository) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //TODO implement a way to check if user exists in DB and if not write the user to DB
    public Map<String, String> validateToken(String idTokenString) {
        Map<String, String> response = new HashMap<>();

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

                // Create claims map
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", sub);
                claims.put("email", email);
                claims.put("name", name);
                claims.put("picture", picture);
                claims.put("aud", aud);
                // Add any other claims you need

                // Generate JWT token
                String jwt = jwtTokenGenerator.generateToken(claims)
                        .orElseThrow(() -> new RuntimeException("Failed to generate JWT"));

                UserEntity userEntity = new UserEntity(sub, name, email, picture, LocalDateTime.now());

                userService.saveOrUpdateUser(userEntity)
                        .subscribe(user -> {});

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