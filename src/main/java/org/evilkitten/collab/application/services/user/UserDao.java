package org.evilkitten.collab.application.services.user;

import org.evilkitten.collab.application.entity.ProviderType;
import org.evilkitten.collab.application.entity.User;

public interface UserDao {
    User getById(Integer id);

    User getByProvider(ProviderType providerType, String providerAccountId);

    User add(User user);
}
