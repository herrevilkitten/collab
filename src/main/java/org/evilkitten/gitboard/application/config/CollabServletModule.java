package org.evilkitten.gitboard.application.config;

import com.google.inject.servlet.ServletModule;
import org.evilkitten.gitboard.application.security.UserAuthenticatedFilter;

public class CollabServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(UserAuthenticatedFilter.class);
    }
}
