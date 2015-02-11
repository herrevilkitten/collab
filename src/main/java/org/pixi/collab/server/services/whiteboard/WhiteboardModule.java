package org.pixi.collab.server.services.whiteboard;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class WhiteboardModule extends AbstractModule {
    @Override
    protected void configure() {
        System.err.println("Configuring WhiteboardModule");
        bind(WhiteboardSession.class);
        bind(WhiteboardTasks.class).in(Singleton.class);
    }
}
