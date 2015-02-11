package org.pixi.collab.server.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.nnsoft.guice.guartz.QuartzModule;
import org.pixi.collab.server.services.JspModule;
import org.pixi.collab.server.services.provider.ProviderModule;
import org.pixi.collab.server.services.user.UserModule;
import org.pixi.collab.server.services.whiteboard.WhiteboardModule;
import org.pixi.collab.server.services.whiteboard.WhiteboardTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollabGuiceServletConfig extends GuiceServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(CollabGuiceServletConfig.class);
    public static Injector injector;

    @Override
    protected Injector getInjector() {
        LOG.info("Configuring " + getClass().getName());
        injector = Guice.createInjector(
            new UserModule(),
            new JspModule(),
            new CollabServletModule(),
            new ConfigurationModule(),
            new DatabaseConfigurationModule(),
            new WhiteboardModule(),
            new ProviderModule(),
            new QuartzModule() {
                @Override
                protected void schedule() {
                    this.scheduleJob(WhiteboardTasks.class);
                }
            }
        );
        return injector;
    }
}
