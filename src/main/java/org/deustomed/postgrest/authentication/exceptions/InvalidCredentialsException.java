package org.deustomed.postgrest.authentication.exceptions;

public class InvalidCredentialsException extends PostgrestAuthenticationException {
    public InvalidCredentialsException() {
        super();
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

