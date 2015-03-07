package org.evilkitten.collab.application.services.whiteboard;

import java.util.ArrayList;
import java.util.List;

public class WhiteboardSession {
    private Integer id;
    private Integer creator;
    private final List<WhiteboardAction> actions = new ArrayList<>();


    public List<WhiteboardAction> getActions() {
        return actions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }
}
