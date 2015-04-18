package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

public interface WhiteboardService {
    Whiteboard getRawById(Integer id);

    Whiteboard getById(Integer id);

    List<Whiteboard> getAllByCreator(User creator);

    Whiteboard create(User creator, String name);

    BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard);
}
