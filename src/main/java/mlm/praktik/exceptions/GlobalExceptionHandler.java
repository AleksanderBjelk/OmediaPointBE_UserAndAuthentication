package mlm.praktik.exceptions;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.MediaType;
import jakarta.inject.Singleton;

@Singleton
@Produces(MediaType.TEXT_PLAIN)
public class GlobalExceptionHandler {

    // 404 NOT FOUND - Data not found from API
    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<String> handleNoDataException(MessageExceptionHandler.NoDataException ex) {
        return HttpResponse.notFound(ex.getMessage());
    }

    // 500 INTERNAL SERVER ERROR - Something is wrong with database or backend
    @Error(status = HttpStatus.INTERNAL_SERVER_ERROR, global = true)
    public HttpResponse<String> handleDatabaseException(MessageExceptionHandler.DatabaseOperationException ex) {
        return HttpResponse.serverError(ex.getMessage());
    }

    // 400 BAD REQUEST - FE sends wrong input to BE
    @Error(status = HttpStatus.BAD_REQUEST, global = true)
    public HttpResponse<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return HttpResponse.badRequest("Invalid input: " + ex.getMessage());
    }

    // 401 UNAUTHORIZED - User is not logged in or missing token
    @Error(status = HttpStatus.UNAUTHORIZED, global = true)
    public HttpResponse<String> handleIllegalStateException(IllegalStateException ex) {
        return HttpResponse.unauthorized().body("Invalid state: " + ex.getMessage());
    }

    // 500 INTERNAL SERVER ERROR - All unexpected errors
    @Error(status = HttpStatus.INTERNAL_SERVER_ERROR, global = true)
    public HttpResponse<String> handleGlobalException(Exception ex) {
        return HttpResponse.serverError("Ett oväntat fel uppstod: " + ex.getMessage());
    }

    // 409 CONFLICT - If there are duplicates
    @Error(status = HttpStatus.CONFLICT, global = true)
    public HttpResponse<String> handleConflictException(MessageExceptionHandler.ConflictException ex) {
        return HttpResponse.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // 403 FORBIDDEN - User is missing rights
    @Error(status = HttpStatus.FORBIDDEN, global = true)
    public HttpResponse<String> handleForbiddenException(MessageExceptionHandler.ForbiddenException ex) {
        return HttpResponse.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // 422 UNPROCESSABLE ENTITY - Input is right but not authorized but technically right
    @Error(status = HttpStatus.UNPROCESSABLE_ENTITY, global = true)
    public HttpResponse<String> handleUnprocessableEntityException(MessageExceptionHandler.UnprocessableEntityException ex) {
        return HttpResponse.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}