package com.english.exceptions;

import java.io.IOException;

public class UserTokenNotFoundException extends IOException {
    public UserTokenNotFoundException() {
    }

    public UserTokenNotFoundException(String message) {
        super(message);
    }

    public UserTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
