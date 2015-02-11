package org.pixi.collab.server.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigurationModule extends AbstractModule {
    private Config configuration;

    @Override
    protected void configure() {
        configuration = ConfigFactory.load();
    }

    @Provides
    public Config getConfiguration() {
        return configuration;
    }
}
