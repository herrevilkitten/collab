package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardAction;

public class WelcomeMessage extends GitboardMessage {
    String uuid;
    final List<WhiteboardAction> actions = new ArrayList<>();

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
