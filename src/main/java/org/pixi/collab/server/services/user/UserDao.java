package org.pixi.collab.server.services.user;

import java.sql.SQLException;

import org.pixi.collab.server.entity.ProviderType;
import org.pixi.collab.server.entity.User;

public interface UserDao {
    User getById(Integer id) throws SQLException;

    User getByProvider(ProviderType providerType, String providerAccountId) throws SQLException;

    User add(User user) throws SQLException;
}
