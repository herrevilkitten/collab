package org.evilkitten.collab.application;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.evilkitten.collab.application.config.CollabGuiceServletConfig;
import org.evilkitten.collab.application.config.Jackson2Feature;
import org.evilkitten.collab.application.config.exceptionmapper.GlobalExceptionMapper;
import org.evilkitten.collab.application.config.exceptionmapper.JerseyExceptionMapper;
import org.evilkitten.collab.application.services.JspWeb;
import org.evilkitten.collab.application.services.atmosphere.MessageWeb;
import org.evilkitten.collab.application.services.user.UserWeb;
import org.evilkitten.collab.application.services.whiteboard.WhiteboardWeb;
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

        // Register the service classes by scanning the classpath
        LOG.info("Registering services");
        packages("org.evilkitten.collab.application");

        LOG.info("Initializing guice bridge");
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(CollabGuiceServletConfig.injector);
    }
}
