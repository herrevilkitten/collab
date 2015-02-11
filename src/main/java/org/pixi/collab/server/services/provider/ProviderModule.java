package org.pixi.collab.server.services.provider;

import com.google.inject.AbstractModule;

public class ProviderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ProviderDao.class).to(JooqProviderDao.class);
    }
}
