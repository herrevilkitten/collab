package org.evilkitten.collab.application.services.provider;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.typesafe.config.Config;
import org.evilkitten.collab.application.database.query.Statement;
import org.evilkitten.collab.application.entity.Provider;

public class PostgresProviderDao implements ProviderDao {
    private final Config config;
    private final DataSource dataSource;

    @Inject
    public PostgresProviderDao(Config config, DataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
    }

    @Override
    public Provider getById(Integer id) throws SQLException {
        Statement statement = new Statement(config.getString("collab.private.database.sql.provider.getById"));

        statement.set("id", id);
        return statement.queryForRow(dataSource, new ProviderRowMapper());
    }

    @Override
    public Provider getByName(String name) throws SQLException {
        Statement statement = new Statement(config.getString("collab.private.database.sql.provider.getByName"));

        statement.set("name", name);
        return statement.queryForRow(dataSource, new ProviderRowMapper());
    }
}
