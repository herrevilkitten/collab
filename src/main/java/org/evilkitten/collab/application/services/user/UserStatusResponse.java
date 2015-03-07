package org.evilkitten.collab.application.services.user;

import javax.ws.rs.core.Response;

import org.evilkitten.collab.application.entity.User;
import org.evilkitten.collab.application.response.CollabResponse;

public class UserStatusResponse extends CollabResponse {
    private boolean signedIn;
    private User user;

    public UserStatusResponse() {
        this(null);
    }

    public UserStatusResponse(User user) {
        super(Response.Status.OK);

        this.user = user;
        this.signedIn = (user != null);
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public User getUser() {
        return user;
    }
}

