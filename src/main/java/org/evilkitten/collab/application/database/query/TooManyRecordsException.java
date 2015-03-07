package org.evilkitten.collab.application.database.query;

public class TooManyRecordsException extends RuntimeException {
    public TooManyRecordsException() {
        super();
    }

    public TooManyRecordsException(String message) {
        super(message);
    }

    public TooManyRecordsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyRecordsException(Throwable cause) {
        super(cause);
    }

    protected TooManyRecordsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
