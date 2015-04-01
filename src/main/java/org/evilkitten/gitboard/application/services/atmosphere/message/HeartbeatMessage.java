package org.evilkitten.gitboard.application.services.atmosphere.message;

public class HeartbeatMessage extends GitboardMessage {

    @Override
    public String getType() {
        return "heartbeat";
    }
}
