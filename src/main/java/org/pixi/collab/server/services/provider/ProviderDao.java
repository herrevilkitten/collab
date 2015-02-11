package org.pixi.collab.server.services.provider;

import java.sql.SQLException;

import org.pixi.collab.server.entity.Provider;

public interface ProviderDao {
    Provider getById(Integer id) throws SQLException;

    Provider getByName(String name) throws SQLException;
}
