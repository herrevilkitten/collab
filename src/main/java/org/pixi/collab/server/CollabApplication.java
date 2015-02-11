package org.pixi.collab.server;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.pixi.collab.server.config.CollabGuiceServletConfig;
import org.pixi.collab.server.config.Jackson2Feature;
import org.pixi.collab.server.config.exceptionmapper.GlobalExceptionMapper;
import org.pixi.collab.server.config.exceptionmapper.JerseyExceptionMapper;
import org.pixi.collab.server.services.JspWeb;
import org.pixi.collab.server.services.atmosphere.MessageWeb;
import org.pixi.collab.server.services.user.UserWeb;
import org.pixi.collab.server.services.whiteboard.WhiteboardWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CollabApplication extends ResourceConfig {
    private static final Logger LOG = LoggerFactory.getLogger(CollabApplication.class);

    /**
     * @param serviceLocator
     */
    @Inject
    public CollabApplication(ServiceLocator serviceLocator) {
        super(Jackson2Feature.class, MultiPartFeature.class);

        LOG.info("Initializing CollabApplication");

        property(JspMvcFeature.TEMPLATES_BASE_PATH, "/");
        register(JspMvcFeature.class);

        LOG.info("Initializing guice bridge");
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(CollabGuiceServletConfig.injector);

        // Register the service classes, starting with the main view
        LOG.info("Registering services");
        register(JspWeb.class);
        register(UserWeb.class);
        register(WhiteboardWeb.class);
        register(MessageWeb.class);

        // Register the exception mappers
        LOG.info("Registering exception mappers");
        register(GlobalExceptionMapper.class);
        register(JerseyExceptionMapper.class);
    }
}
