package org.pixi.collab.server.config;

import com.google.inject.AbstractModule;

public class DatabaseConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DatabaseConfiguration.class).to(C3p0DatabaseConfiguration.class);
    }
}
