package org.evilkitten.gitboard.application.services.user;

import java.sql.SQLException;

import org.evilkitten.gitboard.application.database.query.RowMapper;
import org.evilkitten.gitboard.application.database.query.UncheckedResultSet;
import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(UncheckedResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setDisplayName(resultSet.getString("displayName"));
        user.setProviderType(ProviderType.valueOf(resultSet.getString("providerType")));
        user.setProviderId(resultSet.getString("providerAccountId"));

        return user;
    }
}
