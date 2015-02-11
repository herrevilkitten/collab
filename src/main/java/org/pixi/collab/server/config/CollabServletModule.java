package org.pixi.collab.server.config;

import com.google.inject.servlet.ServletModule;
import org.pixi.collab.server.security.UserAuthenticatedFilter;

public class CollabServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(UserAuthenticatedFilter.class);
    }
}
