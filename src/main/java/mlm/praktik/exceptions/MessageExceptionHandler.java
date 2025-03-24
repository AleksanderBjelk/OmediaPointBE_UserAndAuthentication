package mlm.praktik.exceptions;


import io.micronaut.core.annotation.Introspected;

@Introspected
public class MessageExceptionHandler {
    public static class NoDataException extends RuntimeException {
        public NoDataException(String message) {
            super(message);
        }
    }

    public static class RateLimitException extends RuntimeException {
        public RateLimitException(String message) {
            super(message);
        }
    }

    public static class DatabaseOperationException extends RuntimeException {
        public DatabaseOperationException(String message) {
            super(message);
        }
    }

    public static class ConflictException extends RuntimeException {
        public ConflictException(String message) {super(message);}
    }

    public static class ForbiddenException extends RuntimeException {
        public ForbiddenException(String message) {super(message);}
    }

    public static class UnprocessableEntityException extends RuntimeException {
        public UnprocessableEntityException(String message) {super(message);}
    }

    public static class InvalidInput extends RuntimeException {
        public InvalidInput(String message) {super(message);}
    }


}
