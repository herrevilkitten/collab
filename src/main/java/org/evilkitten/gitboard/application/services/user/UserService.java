package org.evilkitten.gitboard.application.services.user;

import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;

public interface UserService {
    User getById(Integer id);

    User getByProvider(ProviderType providerType, String providerAccountId);

    User add(User user);
}
