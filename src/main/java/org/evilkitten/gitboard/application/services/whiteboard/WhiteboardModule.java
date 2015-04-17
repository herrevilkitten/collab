package org.evilkitten.gitboard.application.services.whiteboard;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhiteboardModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(WhiteboardModule.class);

    @Override
    protected void configure() {
        LOG.info("Configuring WhiteboardModule");

        bind(WhiteboardService.class).to(DefaultWhiteboardService.class);
        bind(WhiteboardDao.class).to(PostgresqlWhiteboardDao.class);
        bind(WhiteboardTasks.class).in(Singleton.class);
    }
}
