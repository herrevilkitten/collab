package org.evilkitten.collab.application.services.whiteboard;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import org.evilkitten.collab.application.response.CollabResponse;

public class WhiteboardActionResponse extends CollabResponse {
    private final List<WhiteboardAction> actions = new ArrayList<>();
    private int lastIndex;

    public WhiteboardActionResponse(int status) {
        super(status);
    }

    public WhiteboardActionResponse(Response.Status status) {
        super(status);
    }

    public List<WhiteboardAction> getActions() {
        return actions;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }
}
