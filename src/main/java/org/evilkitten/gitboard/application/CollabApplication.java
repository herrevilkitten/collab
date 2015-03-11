package org.evilkitten.gitboard.application;

import javax.inject.Inject;

import org.evilkitten.gitboard.application.config.CollabGuiceServletConfig;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.evilkitten.gitboard.application.config.Jackson2Feature;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardWeb;
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
        packages("org.evilkitten.gitboard.application");

        LOG.info("Initializing guice bridge");
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(CollabGuiceServletConfig.injector);
    }
}
