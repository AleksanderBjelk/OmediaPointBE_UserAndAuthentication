package mlm.praktik.controllers;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpRequest;
import mlm.praktik.services.AuthService;  // New import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Controller("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService = new AuthService();

    @Get("/token")
    public Map<String, String> token(HttpRequest<?> request) {
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idTokenString = authHeader.substring(7);
            return authService.validateToken(idTokenString);
        } else {
            Map<String, String> response = new HashMap<>();
            logger.warn("No valid token found in the request");
            response.put("message", "No valid token found.");
            return response;
        }
    }
}
