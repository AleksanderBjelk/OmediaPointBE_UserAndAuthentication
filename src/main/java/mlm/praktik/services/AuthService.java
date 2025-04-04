package mlm.praktik.services;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import java.util.Base64;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import mlm.praktik.entities.UserEntity;
import mlm.praktik.models.UserModel;
import mlm.praktik.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    UserModel userModel = new UserModel();

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
                Long exp = System.currentTimeMillis() + 300000;

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
            SignedJWT signedJWT = SignedJWT.parse(accessTokenString);

            byte[] secretBytes = Base64.getDecoder().decode("KCvxfSSBImPIqQrYOBvr1sGOHFeEfvMsQcqX3eh6fKM=");

            JWSVerifier verifier = new MACVerifier(secretBytes);

            if (!signedJWT.verify(verifier)) {
                logger.warn("Invalid token signature.");
                response.put("message", "Invalid token signature");
                return response;
            }

            var claimsSet = signedJWT.getJWTClaimsSet();
            String sub = claimsSet.getStringClaim("sub");
            String aud = claimsSet.getStringClaim("aud");

            logger.info("Token is valid. Token info: {}, {}", sub, aud);


            userRepository.findById(sub)
                    .subscribe(user -> {
                        userModel = new UserModel(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getPicture(),
                                user.getRole()
                        );
                    });

            Long jwtExp = System.currentTimeMillis() + 3600000;


            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put("sub", sub);
            newClaims.put("exp", jwtExp);
            newClaims.put("aud", aud);
            newClaims.put("name", userModel.getName());
            newClaims.put("email", userModel.getEmail());
            newClaims.put("picture", userModel.getPicture());
            newClaims.put("role", userModel.getRole());

            String JWT = jwtTokenGenerator.generateToken(newClaims)
                    .orElseThrow(() -> new RuntimeException("Failed to generate JWT"));

            response.put("message", "Token verified");
            response.put("token", JWT);
        } catch (Exception e) {
            logger.error("Token validation error: ", e);
            response.put("message", "Token validation error: " + e.getMessage());
        }

        return response;
    }
}