package org.evilkitten.collab.application.services.atmosphere;

import javax.servlet.http.HttpServlet;

import java.util.UUID;

import com.google.inject.Injector;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Post;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.evilkitten.collab.application.config.CollabGuiceServletConfig;
import org.evilkitten.collab.application.services.atmosphere.message.ActionMessage;
import org.evilkitten.collab.application.services.atmosphere.message.CollabMessage;
import org.evilkitten.collab.application.services.atmosphere.message.HeartbeatMessage;
import org.evilkitten.collab.application.services.atmosphere.message.WelcomeMessage;
import org.evilkitten.collab.application.services.whiteboard.WhiteboardAddAction;
import org.evilkitten.collab.application.services.whiteboard.WhiteboardSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedService(path = "/chat")
public class MessageWeb extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MessageWeb.class);

    final static WhiteboardSession whiteboardSession = new WhiteboardSession();

    private static Injector injector;

    public MessageWeb() {
        LOG.info("Creating new MessageWeb");
        this.injector = CollabGuiceServletConfig.injector;
    }

    @Ready(encoders = {JacksonEncoder.class})
    public WelcomeMessage onReady(final AtmosphereResource resource) {
        LOG.info("Browser {} connected", resource.uuid());

        WelcomeMessage wm = new WelcomeMessage();
        wm.setUuid(resource.uuid());
        wm.getActions().addAll(whiteboardSession.getActions());
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
    public CollabMessage onMessage(CollabMessage message) {
        LOG.info("{} sent [{}] {}", message.getAuthor(), message.getType(), message.getMessage());
        if (message instanceof HeartbeatMessage) {
            return null;
        } else if (message instanceof ActionMessage) {
            WhiteboardAddAction wbAction = new WhiteboardAddAction();
            wbAction.setId(UUID.randomUUID().toString());
            wbAction.setActor(message.getAuthor());
            wbAction.setType(message.getType());
            wbAction.setObject((ActionMessage) message);

            whiteboardSession.getActions().add(wbAction);
            LOG.info("Action: {}", message);
        }
        return message;
    }
}
