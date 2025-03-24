package mlm.praktik.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import mlm.praktik.exceptions.MessageExceptionHandler;

@Controller("/test-exception")
public class TestExceptionController {

    @Get("/not-found")
    public void throwNotFound() {
        throw new MessageExceptionHandler.NoDataException("Test: Data not found");
    }

    @Get("/database-error")
    public void throwDatabaseError() {
        throw new MessageExceptionHandler.DatabaseOperationException("Test: Database error");
    }

    @Get("/bad-request")
    public void throwBadRequest() {
        throw new IllegalArgumentException("Test: Invalid input");
    }

    @Get("/unauthorized")
    public void throwUnauthorized() {
        throw new IllegalStateException("Test: Unauthorized action");
    }

    @Get("/conflict")
    public void throwConflict() {
        throw new MessageExceptionHandler.ConflictException("Test: Conflict detected");
    }

    @Get("/forbidden")
    public void throwForbidden() {
        throw new MessageExceptionHandler.ForbiddenException("Test: Forbidden access");
    }

    @Get("/unprocessable-entity")
    public void throwUnprocessableEntity() {
        throw new MessageExceptionHandler.UnprocessableEntityException("Test: Unprocessable input");
    }

    @Get("/unexpected-error")
    public void throwUnexpectedError() {
        throw new RuntimeException("Test: Unexpected error");
    }
}