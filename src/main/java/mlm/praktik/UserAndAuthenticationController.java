package mlm.praktik;

import io.micronaut.http.annotation.*;

@Controller("/userAndAuthentication")
public class UserAndAuthenticationController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}