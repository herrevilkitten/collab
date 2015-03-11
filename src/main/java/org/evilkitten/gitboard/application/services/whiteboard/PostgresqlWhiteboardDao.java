package org.evilkitten.gitboard.application.services.whiteboard;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.typesafe.config.Config;

public class PostgresqlWhiteboardDao {
    private Config config;
    private DataSource dataSource;

    @Inject
    public PostgresqlWhiteboardDao(Config config, DataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
    }
}
