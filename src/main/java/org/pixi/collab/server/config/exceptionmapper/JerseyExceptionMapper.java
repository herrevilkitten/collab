package org.pixi.collab.server.config.exceptionmapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.pixi.collab.server.response.CollabResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private static final Logger LOG = LoggerFactory.getLogger(JerseyExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        LOG.error(exception.toString(), exception);

        CollabResponse response = new CollabResponse(exception.getResponse().getStatus());

        response.getMessages().add(exception.getMessage());

        return Response.status(response.getStatus()).entity(response).build();
    }
}
