package org.evilkitten.gitboard.application.exception;

public class GitboardException extends RuntimeException {
    public GitboardException() {
    }

    public GitboardException(String message) {
        super(message);
    }

    public GitboardException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitboardException(Throwable cause) {
        super(cause);
    }

    public GitboardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
