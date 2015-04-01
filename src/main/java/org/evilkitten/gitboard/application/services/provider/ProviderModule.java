package org.evilkitten.gitboard.application.services.provider;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(ProviderModule.class);

    @Override
    protected void configure() {
        LOG.info("Configuring ProviderModule");
        bind(ProviderDao.class).to(PostgresProviderDao.class);
    }
}
