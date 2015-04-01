package org.evilkitten.gitboard.application.services.user;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(UserModule.class);

    @Override
    protected void configure() {
        LOG.info("Configuring UserModule");

        bind(UserDao.class).to(PostgresqlUserDao.class);
        bind(UserService.class).to(DefaultUserService.class);
        bind(CurrentUser.class).in(SessionScoped.class);
    }
}
