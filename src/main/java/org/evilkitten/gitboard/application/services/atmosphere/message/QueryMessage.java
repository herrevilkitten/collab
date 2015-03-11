package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.evilkitten.gitboard.application.services.whiteboard.WhiteboardAction;

@Data
public class QueryMessage extends CollabMessage {
    final List<WhiteboardAction> actions = new ArrayList<>();
}