package org.evilkitten.gitboard.application.services.whiteboard;

import lombok.Data;

@Data
public class AccessWhiteboard {
    private Mode mode;
    private String email;

    public enum Mode {
        ADD,
        REMOVE
    }
}
