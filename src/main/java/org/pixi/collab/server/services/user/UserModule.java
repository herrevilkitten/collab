package org.pixi.collab.server.services.user;

import com.google.inject.AbstractModule;

public class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserDao.class).to(JooqUserDao.class);
    }
}
