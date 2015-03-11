package org.evilkitten.gitboard.application.services.provider;

import java.sql.SQLException;

import org.evilkitten.gitboard.application.entity.Provider;

public interface ProviderDao {
    Provider getById(Integer id) throws SQLException;

    Provider getByName(String name) throws SQLException;
}
