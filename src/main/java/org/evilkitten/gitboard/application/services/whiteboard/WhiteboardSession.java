package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WhiteboardSession {
    private Integer id = 1;
    private Integer creator;
    private final List<WhiteboardAction> actions = new ArrayList<>();
}
