package mlm.praktik.unit;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class TestExceptions {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testNotFoundException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/not-found");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Test: Data not found", response.body());
    }

    @Test
    void testDatabaseException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/database-error");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("Test: Database error", response.body());
    }

    @Test
    void testBadRequestException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/bad-request");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Invalid input: Test: Invalid input", response.body());
    }

    @Test
    void testUnauthorizedException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/unauthorized");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid state: Test: Unauthorized action", response.body());
    }

    @Test
    void testConflictException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/conflict");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatus());
        assertEquals("Test: Conflict detected", response.body());
    }

    @Test
    void testForbiddenException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/forbidden");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("Test: Forbidden access", response.body());
    }

    @Test
    void testUnprocessableEntityException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/unprocessable-entity");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatus());
        assertEquals("Test: Unprocessable input", response.body());
    }

    @Test
    void testUnexpectedErrorException() {
        HttpRequest<?> request = HttpRequest.GET("/test-exception/unexpected-error");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("Ett oväntat fel uppstod: Test: Unexpected error", response.body());
    }
}
