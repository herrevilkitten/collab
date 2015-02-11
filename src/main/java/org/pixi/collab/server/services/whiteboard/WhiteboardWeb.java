package org.pixi.collab.server.services.whiteboard;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/board")
public class WhiteboardWeb {
    private static final Logger LOG = LoggerFactory.getLogger(WhiteboardWeb.class);

    final static WhiteboardSession whiteboardSession = new WhiteboardSession();

    @GET
    @Path("/actions")
    @Produces(MediaType.APPLICATION_JSON)
    public WhiteboardActionResponse getAllActions(@QueryParam("from") final int from) {
        List<WhiteboardAction> actions = whiteboardSession.getActions();

        WhiteboardActionResponse response = new WhiteboardActionResponse(200);

        if (from < actions.size()) {
            if (from <= 0) {
                response.getActions().addAll(actions);
            } else {
                response.getActions().addAll(actions.subList(from - 1, actions.size()));
            }
        }
        response.setLastIndex(actions.size());

        return response;
    }
}
