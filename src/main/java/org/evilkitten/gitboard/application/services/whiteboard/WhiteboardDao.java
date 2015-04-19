package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

public interface WhiteboardDao {
    Whiteboard getById(Integer id);

    List<Whiteboard> getAllByCreator(User creator);

    List<Whiteboard> getAllByAccess(User user);

    Whiteboard create(User creator, String name);

    BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard);

    List<BaseShape> getShapesForWhiteboard(Integer id);
}
