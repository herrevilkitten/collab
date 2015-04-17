package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.evilkitten.gitboard.application.services.user.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/board")
public class WhiteboardWeb {
    private static final Logger LOG = LoggerFactory.getLogger(WhiteboardWeb.class);

    final static Whiteboard WHITEBOARD = new Whiteboard();

    private final CurrentUser currentUser;
    private final WhiteboardDao whiteboardDao;

    @Inject
    public WhiteboardWeb(CurrentUser currentUser, WhiteboardDao whiteboardDao) {
        this.currentUser = currentUser;
        this.whiteboardDao = whiteboardDao;
    }

    @GET
    @Path("{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Whiteboard getById(@PathParam("boardId") Integer boardId) {
        return whiteboardDao.getById(boardId);
    }

    @GET
    @Path("/byUser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Whiteboard> getAllByUser() {
        return whiteboardDao.getAllByCreator(currentUser.get());
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(NewWhiteboard newWhiteboard) {
        LOG.info("User {} creating new board: {}", currentUser.get(), newWhiteboard);
        Whiteboard board = whiteboardDao.create(currentUser.get(), newWhiteboard.getName());
        return Response.status(Response.Status.CREATED).entity(board).build();
    }

    @GET
    @Path("/actions")
    @Produces(MediaType.APPLICATION_JSON)
    public WhiteboardActionResponse getAllActions(@QueryParam("from") final int from) {
        List<WhiteboardAction> actions = WHITEBOARD.getActions();

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
