package org.evilkitten.gitboard.application.services.provider;

import java.sql.SQLException;

import org.evilkitten.gitboard.application.database.query.RowMapper;
import org.evilkitten.gitboard.application.database.query.UncheckedResultSet;
import org.evilkitten.gitboard.application.entity.Provider;

public class ProviderRowMapper implements RowMapper<Provider> {
    @Override
    public Provider mapRow(UncheckedResultSet resultSet) throws SQLException {
        Provider provider = new Provider();

        provider.setId(resultSet.getInt("id"));
        provider.setName(resultSet.getString("name"));

        return provider;
    }
}
