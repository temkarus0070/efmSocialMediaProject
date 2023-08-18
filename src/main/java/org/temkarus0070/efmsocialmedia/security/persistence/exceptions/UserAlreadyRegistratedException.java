package org.temkarus0070.efmsocialmedia.security.persistence.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegistratedException extends AuthenticationException {

    public UserAlreadyRegistratedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAlreadyRegistratedException(String msg) {
        super(msg);
    }
}
