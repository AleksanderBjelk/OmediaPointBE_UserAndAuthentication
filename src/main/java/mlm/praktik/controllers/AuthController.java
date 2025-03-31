package mlm.praktik.controllers;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mlm.praktik.services.AuthService;
import java.util.HashMap;
import java.util.Map;

@Controller("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Get("/token")
    public Map<String, String> token(HttpRequest<?> request) {
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idTokenString = authHeader.substring(7);
            try {
                Map<String, String> response = authService.validateToken(idTokenString);
                if (response.containsKey("message") && response.get("message").equals("Invalid ID token")) {
                    logger.warn("Invalid ID token");
                    response.put("message", "The provided token is invalid.");
                }
                return response;
            } catch (Exception e) {
                logger.error("Token validation failed: ", e);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Token validation error: " + e.getMessage());
                return errorResponse;
            }
        } else {
            logger.warn("No valid token found in the request");
            Map<String, String> response = new HashMap<>();
            response.put("message", "No valid token found.");
            return response;
        }
    }
}
