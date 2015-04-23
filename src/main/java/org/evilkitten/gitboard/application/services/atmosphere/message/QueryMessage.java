package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import org.evilkitten.gitboard.application.services.whiteboard.Whiteboard;

@Data
public class QueryMessage extends GitboardMessage {
    private Whiteboard whiteboard;
}
