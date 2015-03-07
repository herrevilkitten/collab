package org.evilkitten.collab.application.services.provider;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.evilkitten.collab.application.database.query.RowMapper;
import org.evilkitten.collab.application.entity.Provider;

public class ProviderRowMapper implements RowMapper<Provider> {
    @Override
    public Provider mapRow(ResultSet resultSet) throws SQLException {
        Provider provider = new Provider();

        provider.setId(resultSet.getInt("id"));
        provider.setName(resultSet.getString("name"));

        return provider;
    }
}
