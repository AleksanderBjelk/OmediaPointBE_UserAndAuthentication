package mlm.praktik.controllers;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Controller("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Get("/token")
    public Map<String, Object> token(HttpRequest<?> request, Optional<io.micronaut.security.authentication.Authentication> authentication) {
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authentication.isPresent()) {
            logger.info("Authenticated user: {}", authentication.get().getAttributes());
            return authentication.get().getAttributes();
        } else if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No token provided");
            return Map.of("message", "No token provided");
        } else {
            logger.warn("Invalid or expired token");
            return Map.of("message", "Invalid or expired token");
        }
    }
}
