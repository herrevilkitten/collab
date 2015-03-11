package org.evilkitten.gitboard.application.response;

import javax.ws.rs.core.Response;

public class ExceptionResponse extends GitboardResponse {
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
