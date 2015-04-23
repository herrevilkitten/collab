package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;
import java.util.Set;

import de.danielbechler.diff.node.DiffNode;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

public interface WhiteboardService {
    Whiteboard getRawById(Integer id);

    Whiteboard getById(Integer id);

    List<Whiteboard> getAllByCreator(User creator);

    List<Whiteboard> getAllByAccess(User user);

    Whiteboard copy(User creator, Integer sourceId);

    Whiteboard branch(User creator, Integer sourceId);

    Whiteboard create(User creator, String name);

    BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard);

    Set<BaseShape> merge(int branchId);

    DiffNode findDifferences(int sourceId, int destinationId);

    void removeShape(BaseShape shape);

    void removeShapeFromWhiteboard(BaseShape shape, Whiteboard whiteboard);

    void updateShape(BaseShape shape);

    void updateShapeOnWhiteboard(BaseShape shape, Whiteboard whiteboard);
}
