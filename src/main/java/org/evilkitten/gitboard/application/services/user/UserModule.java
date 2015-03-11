package org.evilkitten.gitboard.application.services.user;

import com.google.inject.AbstractModule;

public class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserDao.class).to(PostgresqlUserDao.class);
    }
}
