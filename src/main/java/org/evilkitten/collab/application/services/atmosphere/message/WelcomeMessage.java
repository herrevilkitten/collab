package org.evilkitten.collab.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import org.evilkitten.collab.application.services.whiteboard.WhiteboardAction;

public class WelcomeMessage extends CollabMessage {
    String uuid;
    final List<WhiteboardAction> actions = new ArrayList<>();

    @Override
    public String getType() {
        return "welcome";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<WhiteboardAction> getActions() {
        return actions;
    }
}