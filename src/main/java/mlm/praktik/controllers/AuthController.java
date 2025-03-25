package mlm.praktik.controllers;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Controller("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Get("/token")
    public Map<String, String> token(HttpRequest<?> request) {
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        Map<String, String> response = new HashMap<>();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("Received token: {}", token);
            response.put("message", "Token received");
            response.put("token", token);
        } else {
            logger.warn("No valid token found in the request");
            response.put("message", "No valid token found.");
        }
        return response;
    }
}