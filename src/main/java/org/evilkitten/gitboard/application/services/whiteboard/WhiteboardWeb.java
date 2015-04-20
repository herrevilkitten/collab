package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.danielbechler.diff.node.DiffNode;
import org.evilkitten.gitboard.application.services.user.CurrentUser;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/board")
public class WhiteboardWeb {
    private static final Logger LOG = LoggerFactory.getLogger(WhiteboardWeb.class);

    final static Whiteboard WHITEBOARD = new Whiteboard();

    private final CurrentUser currentUser;
    private final WhiteboardService whiteboardService;

    @Inject
    public WhiteboardWeb(CurrentUser currentUser, WhiteboardService whiteboardService) {
        this.currentUser = currentUser;
        this.whiteboardService = whiteboardService;
    }

    @GET
    @Path("{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Whiteboard getById(@PathParam("boardId") Integer boardId) {
        return whiteboardService.getById(boardId);
    }

    @GET
    @Path("/byUser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Whiteboard> getAllByUser() {
        return whiteboardService.getAllByCreator(currentUser.get());
    }

    @GET
    @Path("/byAccess")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Whiteboard> getAllByAccess() {
        return whiteboardService.getAllByAccess(currentUser.get());
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(NewWhiteboard newWhiteboard) {
        LOG.info("User {} creating new board: {}", currentUser.get(), newWhiteboard);
        Whiteboard board = whiteboardService.create(currentUser.get(), newWhiteboard.getName());
        return Response.status(Response.Status.CREATED).entity(board).build();
    }

    @POST
    @Path("/copy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response copy(CopyWhiteboard copyWhiteboard) {
        LOG.info("User {} copying board: {}", currentUser.get(), copyWhiteboard);
        Whiteboard board = whiteboardService.copy(currentUser.get(), copyWhiteboard.getId());
        return Response.status(Response.Status.CREATED).entity(board).build();
    }

    @POST
    @Path("/branch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response branch(CopyWhiteboard copyWhiteboard) {
        LOG.info("User {} branching board: {}", currentUser.get(), copyWhiteboard);
        Whiteboard board = whiteboardService.branch(currentUser.get(), copyWhiteboard.getId());
        return Response.status(Response.Status.CREATED).entity(board).build();
    }

    @POST
    @Path("/merge")
    @Produces(MediaType.APPLICATION_JSON)
    public Response merge(CopyWhiteboard copyWhiteboard) {
        LOG.info("User {} merging board: {}", currentUser.get(), copyWhiteboard);
        Set<BaseShape> baseShapes = whiteboardService.merge(copyWhiteboard.getId());
        return Response
            .status(baseShapes.isEmpty() ? Response.Status.OK : Response.Status.CONFLICT)
            .entity(baseShapes)
            .build();
    }

    @POST
    @Path("/diff")
    @Produces(MediaType.APPLICATION_JSON)
    public Response diff(DiffWhiteboard diffWhiteboard) {
        DiffNode differences = whiteboardService.findDifferences(diffWhiteboard.getSource(), diffWhiteboard.getDestination());
        return Response.status(Response.Status.OK).build();
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
