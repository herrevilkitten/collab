package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.List;

import javax.inject.Inject;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.evilkitten.gitboard.application.services.whiteboard.shape.ShapeIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWhiteboardService implements WhiteboardService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWhiteboardService.class);

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
        Whiteboard destination = whiteboardDao.create(creator, "Copy of " + source.getName(), sourceId, null);
        destination.setParentId(sourceId);

        for (BaseShape shape : source.getShapes()) {
            destination.getShapes().add(addShapeToWhiteboard(shape, destination));
        }

        return destination;
    }

    @Override
    public Whiteboard branch(User creator, Integer sourceId) {
        Whiteboard original = copy(creator, sourceId);

        Whiteboard destination = whiteboardDao.create(creator, "Fork of " + sourceId, sourceId, original.getId());
        for (BaseShape shape : original.getShapes()) {
            destination.getShapes().add(addShapeToWhiteboard(shape, destination));
        }

        return destination;
    }

    @Override
    public Whiteboard create(User creator, String name) {
        return whiteboardDao.create(creator, name, null, null);
    }

    @Override
    public BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        return whiteboardDao.addShapeToWhiteboard(shape, whiteboard);
    }

    @Override
    public DiffNode findDifferences(int sourceId, int destinationId) {
        Whiteboard source = getById(sourceId);
        Whiteboard destination = getById(destinationId);

        DiffNode root = ObjectDifferBuilder.startBuilding()
            .introspection()
            .setDefaultIntrospector(new ShapeIntrospector())
            .and().build().compare(destination, source);
        LOG.info("Differences are: {}", root);
        root.visit(new DiffNode.Visitor() {
            public void node(DiffNode node, Visit visit) {
                LOG.info("{} => {}", node.getPath(), node.getState());
                if (node.getPath().toString().startsWith("/shapes[")) {
                    visit.dontGoDeeper();
                }
            }
        });
        return root;
    }
}
