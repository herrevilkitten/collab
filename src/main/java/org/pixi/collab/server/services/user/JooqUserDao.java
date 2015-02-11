package org.pixi.collab.server.services.user;

import javax.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record5;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.pixi.collab.server.config.DatabaseConfiguration;
import org.pixi.collab.server.entity.Provider;
import org.pixi.collab.server.entity.ProviderType;
import org.pixi.collab.server.entity.User;
import org.pixi.collab.server.services.provider.ProviderDao;

import static org.pixi.collab.entity.tables.Provider.PROVIDER;
import static org.pixi.collab.entity.tables.User.USER;

public class JooqUserDao implements UserDao {
    private final DatabaseConfiguration databaseConfiguration;
    private final ProviderDao providerDao;

    @Inject
    public JooqUserDao(DatabaseConfiguration databaseConfiguration, ProviderDao providerDao) {
        this.databaseConfiguration = databaseConfiguration;
        this.providerDao = providerDao;
    }

    @Override
    public User getById(Integer id) throws SQLException {
        try (Connection connection = databaseConfiguration.getDataSource().getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            Record5<Integer, String, String, String, String> result = create
                .select(USER.ID, USER.DISPLAY_NAME, USER.EMAIL, PROVIDER.NAME, USER.PROVIDER_ACCOUNT_ID)
                .from(USER)
                .join(PROVIDER)
                .on(USER.PROVIDER_ID.equal(PROVIDER.ID))
                .where(USER.ID.eq(id))
                .fetchOne();

            User user = null;
            if (result != null) {
                user = new User();
                user.setId(result.getValue(USER.ID));
                user.setDisplayName(result.getValue(USER.DISPLAY_NAME));
                user.setEmail(result.getValue(USER.EMAIL));
                user.setProviderType(ProviderType.valueOf(result.getValue(PROVIDER.NAME)));
                user.setProviderId(result.getValue(USER.PROVIDER_ACCOUNT_ID));
            }

            return user;
        }
    }

    @Override
    public User getByProvider(ProviderType providerType, String providerAccountId) throws SQLException {
        try (Connection connection = databaseConfiguration.getDataSource().getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            Record result = create
                .select(USER.ID, USER.DISPLAY_NAME, USER.EMAIL, PROVIDER.NAME, USER.PROVIDER_ACCOUNT_ID)
                .from(USER)
                .join(PROVIDER)
                .on(USER.PROVIDER_ID.equal(PROVIDER.ID))
                .where(PROVIDER.NAME.equalIgnoreCase(providerType.name()))
                .and(USER.PROVIDER_ACCOUNT_ID.equalIgnoreCase(providerAccountId))
                .fetchOne();

            User user = null;
            if (result != null) {
                user = new User();
                user.setId(result.getValue(USER.ID));
                user.setDisplayName(result.getValue(USER.DISPLAY_NAME));
                user.setEmail(result.getValue(USER.EMAIL));
                user.setProviderType(ProviderType.valueOf(result.getValue(PROVIDER.NAME)));
                user.setProviderId(result.getValue(USER.PROVIDER_ACCOUNT_ID));
            }

            return user;
        }
    }

    @Override
    public User add(User user) throws SQLException {
        try (Connection connection = databaseConfiguration.getDataSource().getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.H2);

            Provider provider = providerDao.getByName(user.getProviderType().name());

            Record record = create.insertInto(USER, USER.EMAIL, USER.DISPLAY_NAME, USER.PROVIDER_ID, USER.PROVIDER_ACCOUNT_ID)
                .values(user.getEmail(), user.getDisplayName(), provider.getId(), user.getProviderId())
                .returning(USER.ID)
                .fetchOne();
            user.setId(record.getValue(USER.ID));
            return user;
        }
    }
}
