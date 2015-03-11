package org.evilkitten.gitboard.application.services.user;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.typesafe.config.Config;
import org.evilkitten.gitboard.application.database.query.Statement;
import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;

public class PostgresqlUserDao implements UserDao {
    private final Config config;
    private final DataSource dataSource;

    @Inject
    public PostgresqlUserDao(Config config, DataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
    }

    public User getById(Integer id) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.account.getById"));

        statement.set("id", id);
        return statement.queryForRow(dataSource, new UserRowMapper());
    }

    public User getByProvider(ProviderType providerType, String providerAccountId) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.account.getByProvider"));

        statement.set("providerType", providerType.name().toUpperCase());
        statement.set("providerAccountId", providerAccountId);
        return statement.queryForRow(dataSource, new UserRowMapper());
    }

    public User add(User user) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.account.addAccount"));

        statement.set("email", user.getEmail());
        statement.set("displayName", user.getDisplayName());
        statement.set("providerType", user.getProviderType().name().toUpperCase());
        statement.set("providerAccountId", user.getProviderId());

        statement.update(dataSource);

        return getByProvider(user.getProviderType(), user.getProviderId());
    }
}
