package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.atmosphere.message.AddShapeActionMessage;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

public interface WhiteboardDao {
    Whiteboard getById(Integer id);

    List<Whiteboard> getAllByCreator(User creator);

    Whiteboard create(User creator);

    BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard);
}
