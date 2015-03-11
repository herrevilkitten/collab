package org.evilkitten.gitboard.application.response;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

public class GitboardResponse {
    private int status;

    private List<String> messages = new ArrayList<>();

    public GitboardResponse(int status) {
        setStatus(status);
    }

    public GitboardResponse(Response.Status status) {
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
