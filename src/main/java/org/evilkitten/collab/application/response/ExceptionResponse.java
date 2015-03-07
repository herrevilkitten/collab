package org.evilkitten.collab.application.response;

import javax.ws.rs.core.Response;

public class ExceptionResponse extends CollabResponse {
    final Throwable throwable;

    public ExceptionResponse(int status, Throwable throwable) {
        super(status);
        this.throwable = throwable;
    }

    public ExceptionResponse(Response.Status status, Throwable throwable) {
        super(status);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
