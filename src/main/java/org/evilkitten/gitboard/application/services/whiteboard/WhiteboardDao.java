package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import org.evilkitten.gitboard.application.entity.User;

public interface WhiteboardDao {
    Whiteboard getById(Integer id);

    List<Whiteboard> getAllByCreator(User creator);

    Whiteboard create(User creator);
}
