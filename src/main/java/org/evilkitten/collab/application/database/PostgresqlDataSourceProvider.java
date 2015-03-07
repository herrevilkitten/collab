package org.evilkitten.collab.application.database;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.typesafe.config.Config;
import org.postgresql.ds.PGSimpleDataSource;

@Singleton
public class PostgresqlDataSourceProvider implements DataSourceProvider {
    private DataSource dataSource;

    private final Config config;

    @Inject
    public PostgresqlDataSourceProvider(Config config) {
        this.config = config;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource source = new PGSimpleDataSource();
            source.setServerName(config.getString("collab.private.database.serverName"));
            source.setDatabaseName(config.getString("collab.private.database.databaseName"));
            source.setUser(config.getString("collab.private.database.user"));
            source.setPassword(config.getString("collab.private.database.password"));
            dataSource = source;
        }
        return dataSource;
    }
}
