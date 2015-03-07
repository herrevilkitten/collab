package org.evilkitten.collab.application.services.provider;

import com.google.inject.AbstractModule;

public class ProviderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ProviderDao.class).to(PostgresProviderDao.class);
    }
}
