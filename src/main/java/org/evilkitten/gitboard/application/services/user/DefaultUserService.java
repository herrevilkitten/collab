package org.evilkitten.gitboard.application.services.user;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.evilkitten.gitboard.application.entity.ProviderType;
import org.evilkitten.gitboard.application.entity.User;

public class DefaultUserService implements UserService {
    private final UserDao userDao;
    private final Map<Integer, User> userCache;

    @Inject
    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
        this.userCache = new HashMap<>();
    }

    @Override
    public User getById(Integer id) {
        User user = userCache.get(id);
        if (user == null) {
            user = userDao.getById(id);
            userCache.put(id, user);
        }

        return user;
    }

    @Override
    public User getByProvider(ProviderType providerType, String providerAccountId) {
        User user = userDao.getByProvider(providerType, providerAccountId);
        userCache.put(user.getId(), user);
        return user;
    }

    @Override
    public User add(User user) {
        user = userDao.add(user);
        userCache.put(user.getId(), user);
        return user;
    }
}
