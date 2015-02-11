package org.pixi.collab.server.services.user;

import javax.inject.Inject;

import org.pixi.collab.server.config.DatabaseConfiguration;
import org.pixi.collab.server.entity.ProviderType;
import org.pixi.collab.server.entity.User;
import org.pixi.common.database.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2UserDao implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(H2UserDao.class);

    private final DatabaseConfiguration configuration;

    @Inject
    public H2UserDao(DatabaseConfiguration configuration) {
        this.configuration = configuration;
    }

    public User getById(Integer id) {
        SqlQuery query = new SqlQuery();
        query.appendln(
            "select user.id id,",
            "       user.display_Name displayName,",
            "       user.email email,",
            "       provider.name providerType,",
            "       user.provider_Account_Id providerAccountId",
            " from  collab.User user",
            " join  collab.Provider provider",
            "   on  user.provider_Id = provider.id",
            " where user.id = ?id?"
        );

        query.set("id", id);
        return query.queryForObject(configuration.getDataSource(), new UserRowMapper());
    }

    public User getByProvider(ProviderType providerType, String providerAccountId) {
        SqlQuery query = new SqlQuery();
        query.appendln(
            "select user.id id,",
            "       user.display_Name displayName,",
            "       user.email email,",
            "       provider.name providerType,",
            "       user.provider_Account_Id providerAccountId",
            " from  collab.User user",
            " join  collab.Provider provider",
            "   on  user.provider_Id = provider.id",
            "  and  UPPER(provider.name) = ?providerType?",
            " where user.provider_Account_Id = ?providerAccountId?"
        );

        query.set("providerType", providerType.name().toUpperCase());
        query.set("providerAccountId", providerAccountId);
        return query.queryForObject(configuration.getDataSource(), new UserRowMapper());
    }

    public User add(User user) {
        SqlQuery query = new SqlQuery();
        query.appendln(
            "insert into collab.User(email, display_Name, provider_Id, provider_Account_Id)",
            "     values (?email?,",
            "             ?displayName?,",
            "             (select provider.id from collab.Provider where UPPER(provider.name) = ?providerType?),",
            "            ?providerAccountId?)"
        );
        query.set("email", user.getEmail());
        query.set("displayName", user.getDisplayName());
        query.set("providerType", user.getProviderType().name().toUpperCase());
        query.set("providerAccountId", user.getProviderId());

        query.executeQuery(configuration.getDataSource());

        return getByProvider(user.getProviderType(), user.getProviderId());
    }
}
