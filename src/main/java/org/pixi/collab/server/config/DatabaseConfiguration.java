package org.pixi.collab.server.config;

import javax.sql.DataSource;

public interface DatabaseConfiguration {
    DataSource getDataSource();
}
