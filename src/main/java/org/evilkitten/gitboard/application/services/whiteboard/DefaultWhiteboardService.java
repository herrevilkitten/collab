package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import javax.inject.Inject;

import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

public class DefaultWhiteboardService implements WhiteboardService {
    private final WhiteboardDao whiteboardDao;

    @Inject
    public DefaultWhiteboardService(WhiteboardDao whiteboardDao) {

        this.whiteboardDao = whiteboardDao;
    }

    @Override
    public Whiteboard getRawById(Integer id) {
        return whiteboardDao.getById(id);
    }

    public Whiteboard getById(Integer id) {
        Whiteboard whiteboard = getRawById(id);
        whiteboard.getShapes().addAll(whiteboardDao.getShapesForWhiteboard(id));
        return whiteboard;
    }

    @Override
    public List<Whiteboard> getAllByCreator(User creator) {
        return whiteboardDao.getAllByCreator(creator);
    }

    @Override
    public List<Whiteboard> getAllByAccess(User user) {
        return whiteboardDao.getAllByAccess(user);
    }

    @Override
    public Whiteboard copy(User creator, Integer sourceId) {
        Whiteboard source = getById(sourceId);
        Whiteboard destination = create(creator, "");

        for (BaseShape shape : source.getShapes()) {
            addShapeToWhiteboard(shape, destination);
        }

        return destination;
    }

    @Override
    public Whiteboard create(User creator, String name) {
        return whiteboardDao.create(creator, name);
    }

    @Override
    public BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        return whiteboardDao.addShapeToWhiteboard(shape, whiteboard);
    }
}
