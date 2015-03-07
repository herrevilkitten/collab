package org.evilkitten.collab.application.database;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.typesafe.config.Config;
import org.apache.commons.dbcp2.BasicDataSource;

@Singleton
public class PooledDataSourceProvider implements DataSourceProvider {
    private final Config config;

    private BasicDataSource dataSource;

    @Inject
    public PooledDataSourceProvider(Config config) {
        this.config = config;
    }

    @Override
    public DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();

            String username = config.getString("collab.private.database.username");
            String password = config.getString("collab.private.database.password");

            if (username != null) {
                dataSource.setUsername(username);
            }
            if (password != null) {
                dataSource.setPassword(password);
            }
            dataSource.setDriverClassName(config.getString("collab.private.database.driver"));
            dataSource.setUrl(config.getString("collab.private.database.url"));
            dataSource.setInitialSize(1);
        }
        return dataSource;
    }
}
