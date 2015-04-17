package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

@Data
public class Whiteboard {
    private Integer id = 1;
    private String name = "";
    private User creator;
    private Date creationTime;

    private final List<WhiteboardAction> actions = new ArrayList<>();
    private final List<BaseShape> shapes = new ArrayList<>();
}
