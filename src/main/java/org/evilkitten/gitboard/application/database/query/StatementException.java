package org.evilkitten.gitboard.application.database.query;

public class StatementException extends RuntimeException {
    public StatementException() {
        super();
    }

    public StatementException(String message) {
        super(message);
    }

    public StatementException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatementException(Throwable cause) {
        super(cause);
    }

    protected StatementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
