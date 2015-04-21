package org.evilkitten.gitboard.application.services.atmosphere;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;

import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Post;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.atmosphere.message.ActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.AddShapeMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.GitboardMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.RemoveShapeMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.WelcomeMessage;
import org.evilkitten.gitboard.application.services.json.JsonTranscoder;
import org.evilkitten.gitboard.application.services.whiteboard.Whiteboard;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@ManagedService(path = "{boardId}")
public class MessageWeb extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWeb.class);

    private final WhiteboardService whiteboardService;
    private final JsonTranscoder jsonTranscoder;

    @Inject
    public MessageWeb(WhiteboardService whiteboardService, JsonTranscoder jsonTranscoder) {
        this.whiteboardService = whiteboardService;
        this.jsonTranscoder = jsonTranscoder;
    }

    @Ready(encoders = {JacksonEncoder.class})
    public WelcomeMessage onReady(final AtmosphereResource resource) {
        User user = (User) resource.session().getAttribute("session.user");
        resource.session().setAttribute("session.socket", resource);

        String broadcasterId = resource.getBroadcaster().getID();
        LOG.info("Browser {} ({}) connected to {}", resource.uuid(), user, broadcasterId);

        // "/chat/7777"
        int boardId = Integer.parseInt(broadcasterId.substring(6));
        Whiteboard whiteboard = whiteboardService.getById(boardId);

        WelcomeMessage wm = new WelcomeMessage();
        wm.setUser(user);
        wm.setBoardId(boardId);
        wm.setUuid(resource.uuid());
        wm.getActions().addAll(whiteboard.getActions());
        wm.setWhiteboard(whiteboard);

        return wm;
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        event.getResource().session().removeAttribute("session.socket");
        if (event.isCancelled()) {
            LOG.info("Browser {} unexpectedly disconnected", this, event.getResource().uuid());
        } else {
            LOG.info("Browser {} closed connection", this, event.getResource().uuid());
        }
    }

    @Post
    public void onPost(AtmosphereResource resource) {
        User user = (User) resource.session().getAttribute("session.user");
        String json = resource.getRequest().body().asString();

        GitboardMessage message = (GitboardMessage) jsonTranscoder.fromJson(json, GitboardMessage.class);
        message.setUuid(resource.uuid());

        if (message instanceof ActionMessage) {
            ((ActionMessage) message).setActor(user);
        }

        Whiteboard whiteboard = whiteboardService.getRawById(message.getBoardId());
        if (message instanceof AddShapeMessage) {
            whiteboardService.addShapeToWhiteboard(((AddShapeMessage) message).getShape(), whiteboard);
            resource.getBroadcaster().broadcast(jsonTranscoder.toJson(message));
        } else if (message instanceof RemoveShapeMessage) {
//            whiteboardService.removeShape(((RemoveShapeMessage) message).getShapeId());
        }
    }
}
