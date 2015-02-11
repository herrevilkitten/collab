package org.pixi.collab.server.response;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

public class CollabResponse {
    private int status;

    private List<String> messages = new ArrayList<>();

    public CollabResponse(int status) {
        setStatus(status);
    }

    public CollabResponse(Response.Status status) {
        setStatus(status.getStatusCode());
    }

    public boolean isSuccess() {
        return status >= 200 && status < 400;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }
}
