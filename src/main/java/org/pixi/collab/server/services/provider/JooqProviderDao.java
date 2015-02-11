package org.pixi.collab.server.services.provider;

import javax.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.pixi.collab.server.config.DatabaseConfiguration;
import org.pixi.collab.server.entity.Provider;

import static org.pixi.collab.entity.tables.Provider.PROVIDER;

public class JooqProviderDao implements ProviderDao {
    private final DatabaseConfiguration databaseConfiguration;

    @Inject
    public JooqProviderDao(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    public Provider getById(Integer id) throws SQLException {
        try (Connection connection = databaseConfiguration.getDataSource().getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.H2);

            Record result = create.select(PROVIDER.ID, PROVIDER.NAME)
                .from(PROVIDER)
                .where(PROVIDER.ID.eq(id))
                .fetchAny();

            Provider provider = null;
            if (result != null) {
                provider = new Provider();
                provider.setId(result.getValue(PROVIDER.ID));
                provider.setName(result.getValue(PROVIDER.NAME));
            }
            return provider;
        }

    }

    @Override
    public Provider getByName(String name) throws SQLException {
        try (Connection connection = databaseConfiguration.getDataSource().getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.H2);

            Record result = create.select(PROVIDER.ID, PROVIDER.NAME)
                .from(PROVIDER)
                .where(PROVIDER.NAME.equalIgnoreCase(name))
                .fetchAny();

            Provider provider = null;
            if (result != null) {
                provider = new Provider();
                provider.setId(result.getValue(PROVIDER.ID));
                provider.setName(result.getValue(PROVIDER.NAME));
            }
            return provider;
        }
    }
}