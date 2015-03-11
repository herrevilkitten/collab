package org.evilkitten.gitboard.application.config.exceptionmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.evilkitten.gitboard.application.response.CollabResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable throwable) {
        CollabResponse response = new CollabResponse(Response.Status.INTERNAL_SERVER_ERROR);

        LOG.error(throwable.toString(), throwable);
        response.getMessages().add(throwable.getMessage());

        return Response.status(response.getStatus()).entity(response).build();
    }
}
