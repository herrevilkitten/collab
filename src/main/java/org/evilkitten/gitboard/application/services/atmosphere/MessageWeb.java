package org.evilkitten.gitboard.application.services.atmosphere;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;

import com.google.inject.Injector;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Post;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.evilkitten.gitboard.application.config.GitboardGuiceServletConfig;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.atmosphere.message.ActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.AddShapeActionMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.GitboardMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.HeartbeatMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.QueryMessage;
import org.evilkitten.gitboard.application.services.atmosphere.message.WelcomeMessage;
import org.evilkitten.gitboard.application.services.whiteboard.Whiteboard;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardAddAction;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@ManagedService(path = "{boardId}")
public class MessageWeb extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWeb.class);

    private final WhiteboardDao whiteboardDao;

    @Inject
    public MessageWeb(WhiteboardDao whiteboardDao) {
        LOG.info("Creating new MessageWeb");
        LOG.info("Got a dao: " + whiteboardDao);
        Injector injector = GitboardGuiceServletConfig.injector;
//        this.whiteboardDao = injector.getInstance(WhiteboardDao.class);
        this.whiteboardDao = whiteboardDao;
    }

    @Ready(encoders = {JacksonEncoder.class})
    public WelcomeMessage onReady(final AtmosphereResource resource) {
        String broadcasterId = resource.getBroadcaster().getID();
        LOG.info("Browser {} connected to {}", resource.uuid(), broadcasterId);

        // "/chat/7777"
        int boardId = Integer.parseInt(broadcasterId.substring(6));
        Whiteboard whiteboard = whiteboardDao.getById(boardId);

        WelcomeMessage wm = new WelcomeMessage();
        wm.setBoardId(boardId);
        wm.setUuid(resource.uuid());
        wm.getActions().addAll(whiteboard.getActions());
        return wm;
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        if (event.isCancelled()) {
            LOG.info("Browser {} unexpectedly disconnected", this, event.getResource().uuid());
        } else {
            LOG.info("Browser {} closed connection", this, event.getResource().uuid());
        }
    }

    @Post
    public void onPost(AtmosphereResource resource) {
        resource.session().getAttribute("session.user");

        LOG.info("Last session access for {}: {}", resource.uuid(), resource.session().getLastAccessedTime());
        LOG.info("Message is {}", resource.getRequest().body().asString());
    }

    @Message(encoders = {JacksonEncoder.class}, decoders = {JacksonDecoder.class})
    public GitboardMessage onMessage(AtmosphereResource resource, GitboardMessage message) {
        User user = (User) resource.session().getAttribute("session.user");
        Whiteboard whiteboard = whiteboardDao.getById(message.getBoardId());
        LOG.info("{} sent [#{} {}] {}", user, message.getBoardId(),
            message.getType(), message.toString());
        if (message instanceof HeartbeatMessage) {
            return null;
        } else if (message instanceof QueryMessage) {
            ((QueryMessage) message).getActions().addAll(whiteboard.getActions());
        } else if (message instanceof ActionMessage) {
            if (message instanceof AddShapeActionMessage) {
                whiteboardDao.addShapeToWhiteboard(((AddShapeActionMessage) message).getShape(), whiteboard);
            }
            WhiteboardAddAction wbAction = new WhiteboardAddAction();
            wbAction.setId(UUID.randomUUID().toString());
            wbAction.setActor(user);
            wbAction.setType(message.getType());
            wbAction.setObject((ActionMessage) message);

            whiteboard.getActions().add(wbAction);
            LOG.info("Action: {}", message);
        }
        return message;
    }
}
