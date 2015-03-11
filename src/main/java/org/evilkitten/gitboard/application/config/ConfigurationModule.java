package org.evilkitten.gitboard.application.config;

import java.util.Objects;

import javax.inject.Singleton;
import javax.servlet.ServletContext;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import edu.gatech.gtri.typesafeconfigextensions.factory.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static edu.gatech.gtri.typesafeconfigextensions.factory.ConfigFactory.systemProperties;
import static edu.gatech.gtri.typesafeconfigextensions.forwebapps.WebappConfigs.webappConfigFactory;

public class ConfigurationModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationModule.class);

    protected final ServletContext servletContext;

    @Override
    protected void configure() {

    }

    public ConfigurationModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Singleton
    @Provides
    Config provideConfig() {
        Objects.requireNonNull(servletContext, "servletContext cannot be null");

        Config config = webappConfigFactory(servletContext)
            .withSources(
                ConfigFactory.classpathResource("application"),
                ConfigFactory.classpathResource("postgres"),
                systemProperties()
            )
            .fromLowestToHighestPrecedence()
            .load();

        LOG.info(config.root().render());

        return config;
    }
}
