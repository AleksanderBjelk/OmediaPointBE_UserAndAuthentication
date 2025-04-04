package mlm.praktik.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.micronaut.security.token.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SignatureException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
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
    public Map<String, String> validateIdTokenAndCreateAccessToken(String idTokenString) {
        Map<String, String> response = new HashMap<>();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String sub = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");
                String aud = "omediapoint";
                String exp = String.valueOf(System.currentTimeMillis() + 300000);

                logger.info("Token is valid. Token info: {}, {}", sub, aud);

                //Create claims map
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", sub);
                claims.put("exp", exp);
                claims.put("aud", aud);

                //Generate access token
                String accessToken = jwtTokenGenerator.generateToken(claims)
                        .orElseThrow(() -> new RuntimeException("Failed to generate JWT"));

                UserEntity userEntity = new UserEntity(sub, name, email, picture, LocalDateTime.now());

                userService.saveOrUpdateUser(userEntity)
                        .subscribe(user -> {});

                response.put("message", "Token verified");
                response.put("token", accessToken);

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


    public Map<String, String> validateAccessTokenAndCreateBEJWT(String accessTokenString) {
        Map<String, String> response = new HashMap<>();
        try {
            //TODO Han måste vara full som tror att vi kan använda GoogleIdToken här?
            GoogleIdToken accessToken = verifier.verify(accessTokenString);
            if (accessToken != null) {
                GoogleIdToken.Payload payload = accessToken.getPayload();
                String aud = payload.getAudience();
                String sub = payload.getSubject();
                long exp = payload.getExpirationTimeSeconds();

                if (!aud.equals("omediapoint")) {
                    response.put("message", "Invalid audience in token");
                    return response;
                }

                if (exp < Instant.now().getEpochSecond()) {
                    response.put("message", "Token has expired");
                    return response;
                }

                logger.info("Token is valid. Token info: {}, {}", sub, aud);

                UserEntity userEntity = userRepository.findById(sub).block();
                if (userEntity == null) {
                    response.put("message", "User not found");
                    return response;
                }

                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", userEntity.getId());
                claims.put("email", userEntity.getEmail());
                claims.put("name", userEntity.getName());
                claims.put("roles", userEntity.getRole());
                claims.put("exp", String.valueOf(System.currentTimeMillis() + 3600000));
                claims.put("aud", "omediapoint_backend");

                String beJwt = jwtTokenGenerator.generateToken(claims)
                        .orElseThrow(() -> new RuntimeException("Failed to generate BE_JWT"));

                response.put("message", "Access token verified and BE_JWT created");
                response.put("be_jwt", beJwt);

            } else {
                logger.warn("Invalid Access token.");
                response.put("message", "Invalid access token");
            }
        } catch (Exception e) {
            logger.error("Token validation error: ", e);
            response.put("message", "Token validation error: " + e.getMessage());
        }

        return response;
    }
}