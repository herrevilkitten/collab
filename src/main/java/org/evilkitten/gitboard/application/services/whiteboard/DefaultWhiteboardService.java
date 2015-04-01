package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import javax.inject.Inject;

import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.atmosphere.message.ShapeActionMessage;

public class DefaultWhiteboardService {
    private final WhiteboardDao whiteboardDao;

    @Inject
    public DefaultWhiteboardService(WhiteboardDao whiteboardDao) {

        this.whiteboardDao = whiteboardDao;
    }

    public Whiteboard getById(Integer id) {
        return whiteboardDao.getById(id);
    }

    public List<Whiteboard> getAllByCreator(User creator) {
        return whiteboardDao.getAllByCreator(creator);
    }

    public Whiteboard create(User creator) {
        return whiteboardDao.create(creator);
    }

    public void addShape(Whiteboard whiteboard, ShapeActionMessage action) {

    }
}
