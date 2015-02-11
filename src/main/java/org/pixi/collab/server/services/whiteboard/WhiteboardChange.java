package org.pixi.collab.server.services.whiteboard;

public class WhiteboardChange {
    private String id;
    private String type;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WhiteboardChange{" +
            "id='" + id + '\'' +
            ", data='" + data + '\'' +
            '}';
    }
}
