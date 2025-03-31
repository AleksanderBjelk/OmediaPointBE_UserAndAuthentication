package mlm.praktik.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

//Just a small controller to check if we can ping with our own token.
@Controller("/secure")
public class SecureController {

    Logger logger = LoggerFactory.getLogger(SecureController.class);

    @Get(uri = "/ping", produces = MediaType.APPLICATION_JSON)
    public Map<String, String> securePing() {
        logger.info("Secure ping");
        return Collections.singletonMap("message", "pong");
    }
}
