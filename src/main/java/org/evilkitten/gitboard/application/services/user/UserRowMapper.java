package org.evilkitten.gitboard.application.services.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.database.query.RowMapper;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setDisplayName(resultSet.getString("displayName"));
        user.setProviderType(ProviderType.valueOf(resultSet.getString("providerType")));
        user.setProviderId(resultSet.getString("providerAccountId"));

        return user;
    }
}
