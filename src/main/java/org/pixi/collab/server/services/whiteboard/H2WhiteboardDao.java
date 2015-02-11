package org.pixi.collab.server.services.whiteboard;

import javax.inject.Inject;

import org.pixi.collab.server.config.DatabaseConfiguration;

public class H2WhiteboardDao {
    private DatabaseConfiguration databaseConfiguration;

    @Inject
    public H2WhiteboardDao(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }


}
