package org.evilkitten.gitboard.application.config;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.jcabi.manifests.Manifests;
import org.evilkitten.gitboard.application.database.DatabaseModule;
import org.evilkitten.gitboard.application.services.JspModule;
import org.evilkitten.gitboard.application.services.provider.ProviderModule;
import org.evilkitten.gitboard.application.services.user.UserModule;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardModule;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardTasks;
import org.nnsoft.guice.guartz.QuartzModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollabGuiceServletConfig extends GuiceServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(CollabGuiceServletConfig.class);
    public static Injector injector;
    protected ServletContext servletContext;

    @Override
    protected Injector getInjector() {
        LOG.info("Configuring " + getClass().getName());
        injector = Guice.createInjector(
            new UserModule(),
            new JspModule(),
            new CollabServletModule(),
            new ConfigurationModule(servletContext),
            new DatabaseModule(),
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

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);

        try {
            Manifests.append(servletContext);
        } catch (IOException e) {
            LOG.warn("Manifest not available on context startup.  This should only happen during development", e);
        }
        servletContext.setAttribute("buildInformation", fromManifest("Implementation-Build", "Not available"));
        servletContext.setAttribute("buildHost", fromManifest("Implementation-Build-Host", "Not available"));
    }

    private String fromManifest(String named, String defaultValue) {
        if (Manifests.exists(named)) {
            return Manifests.read(named);
        }
        return defaultValue;
    }
}
