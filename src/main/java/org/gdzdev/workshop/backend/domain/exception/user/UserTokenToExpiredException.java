package org.gdzdev.workshop.backend.domain.exception.user;

public class UserTokenToExpiredException extends RuntimeException {
    public UserTokenToExpiredException(String message) {
        super(message);
    }
}
