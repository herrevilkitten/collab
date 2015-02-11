package org.pixi.collab.server.services.user;

import java.io.Serializable;

public class OAuthSignin implements Serializable {
    private String token;
    private String state;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "OAuthSignin{" +
                "token='" + token + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
