package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.evilkitten.gitboard.application.entity.User;

@Data
public class Whiteboard {
    private Integer id = 1;
    private User creator;
    private Date creationTime;

    private final List<WhiteboardAction> actions = new ArrayList<>();
}
