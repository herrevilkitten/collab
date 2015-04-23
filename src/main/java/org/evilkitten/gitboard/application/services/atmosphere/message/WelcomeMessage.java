package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.Whiteboard;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardAction;

@Data
public class WelcomeMessage extends GitboardMessage {
    private String uuid;
    private User user;
    private final List<WhiteboardAction> actions = new ArrayList<>();
    private Whiteboard whiteboard;
}
