package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.evilkitten.gitboard.application.services.whiteboard.shape.ShapeDifference;
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
    public Set<BaseShape> merge(int branchId) {
        final Whiteboard branch = getById(branchId);
        if (branch.getOriginalId() == null || branch.getParentId() == null) {
            return Collections.emptySet();
        }

        final Whiteboard original = getById(branch.getOriginalId());
        final Whiteboard parent = getById(branch.getParentId());

        LOG.info("Branch:   {}", branch);
        LOG.info("Original: {}", original);
        LOG.info("Parent:   {}", parent);

        LOG.info("Looking for differences between branch and original.");
        DiffNode branchRoot = ObjectDifferBuilder.startBuilding()
            .introspection()
            .setDefaultIntrospector(new ShapeIntrospector())
            .and().build().compare(branch, original);
        LOG.info("Differences are: {}", branchRoot);

        final WhiteboardDifferences branchDifferences = new WhiteboardDifferences();
        branchRoot.visit(new DiffNode.Visitor() {
            public void node(DiffNode node, Visit visit) {
                LOG.info("{} => {}", node.getPath(), node.getState());
                if (node.getPath().toString().startsWith("/shapes[")) {
                    final BaseShape baseValue = (BaseShape) node.canonicalGet(original);
                    final BaseShape workingValue = (BaseShape) node.canonicalGet(branch);
                    if (node.isAdded()) {
                        branchDifferences.getAdditions().add(workingValue);
                    } else if (node.isRemoved()) {
                        branchDifferences.getRemovals().add(baseValue);
                    } else if (node.isChanged()) {
                        branchDifferences.getChanges().add(baseValue);
                    }

                    ShapeDifference shapeDifference = branchDifferences.getShapes().get(baseValue);
                    if (shapeDifference == null) {
                        branchDifferences.getShapes().put(baseValue, new ShapeDifference());
                        shapeDifference = branchDifferences.getShapes().get(baseValue);
                    }

                    if (!node.isAdded()) {
                        shapeDifference.setBranch(workingValue);
                        shapeDifference.setOriginal(baseValue);
                    }

                    final String message = node.getPath() + " changed from " +
                        baseValue + " to " + workingValue;
                    LOG.info(message);
                    visit.dontGoDeeper();
                }
            }
        });

        LOG.info("Looking for differences between parent and original.");
        DiffNode parentRoot = ObjectDifferBuilder.startBuilding()
            .introspection()
            .setDefaultIntrospector(new ShapeIntrospector())
            .and().build().compare(parent, original);
        LOG.info("Differences are: {}", parentRoot);

        final WhiteboardDifferences parentDifferences = new WhiteboardDifferences();
        parentRoot.visit(new DiffNode.Visitor() {
            public void node(DiffNode node, Visit visit) {
                LOG.info("{} => {}", node.getPath(), node.getState());
                if (node.getPath().toString().startsWith("/shapes[")) {
                    final BaseShape baseValue = (BaseShape) node.canonicalGet(original);
                    final BaseShape workingValue = (BaseShape) node.canonicalGet(parent);
                    if (node.isAdded()) {
                        parentDifferences.getAdditions().add(workingValue);
                    } else if (node.isRemoved()) {
                        parentDifferences.getRemovals().add(baseValue);
                    } else if (node.isChanged()) {
                        parentDifferences.getChanges().add(baseValue);
                    }

                    ShapeDifference shapeDifference = branchDifferences.getShapes().get(baseValue);
                    if (shapeDifference == null) {
                        branchDifferences.getShapes().put(baseValue, new ShapeDifference());
                        shapeDifference = branchDifferences.getShapes().get(baseValue);
                    }

                    if (!node.isAdded()) {
                        shapeDifference.setParent(workingValue);
                        shapeDifference.setOriginal(baseValue);
                    }

                    final String message = node.getPath() + " changed from " +
                        baseValue + " to " + workingValue;
                    LOG.info(message);
                    visit.dontGoDeeper();
                }
            }
        });

        Set<BaseShape> conflicts = new HashSet<>();
        for (BaseShape changedShape : branchDifferences.getChanges()) {
            if (conflicts.contains(changedShape)) {
                continue;
            }

            if (parentDifferences.getChanges().contains(changedShape)) {
                LOG.warn("Conflict. Both #{} and #{} have changed {}",
                    branch.getId(),
                    parent.getId(),
                    changedShape);
                BaseShape branchShape = branchDifferences.getShapes().get(changedShape).getBranch();
                BaseShape parentShape = branchDifferences.getShapes().get(changedShape).getParent();
                DiffNode conflictRoot = ObjectDifferBuilder.startBuilding()
                    .introspection()
                    .setDefaultIntrospector(new ShapeIntrospector())
                    .and().build().compare(branchShape, parentShape);
                LOG.info("Branch:   {}", conflictRoot.canonicalGet(branchShape));
                LOG.info("Parent:   {}", conflictRoot.canonicalGet(parentShape));
                LOG.info("Conflict: {}", conflictRoot);
                if (!conflictRoot.isUntouched()) {
                    conflicts.add(changedShape);
                }
            }

            if (parentDifferences.getRemovals().contains(changedShape)) {
                LOG.warn("Conflict. Branch #{} changed and parent #{} removed {}",
                    branch.getId(),
                    parent.getId(),
                    changedShape);
                LOG.info("Differences are: {}", branchRoot);

                conflicts.add(changedShape);
            }
        }

        for (BaseShape changedShape : parentDifferences.getChanges()) {
            if (conflicts.contains(changedShape)) {
                continue;
            }

            if (branchDifferences.getChanges().contains(changedShape)) {
                LOG.warn("Conflict. Both #{} and #{} have changed {}",
                    branch.getId(),
                    parent.getId(),
                    changedShape);
                BaseShape branchShape = branchDifferences.getShapes().get(changedShape).getBranch();
                BaseShape parentShape = branchDifferences.getShapes().get(changedShape).getParent();
                DiffNode conflictRoot = ObjectDifferBuilder.startBuilding()
                    .introspection()
                    .setDefaultIntrospector(new ShapeIntrospector())
                    .and().build().compare(branchShape, parentShape);
                LOG.info("Branch:   {}", conflictRoot.canonicalGet(branchShape));
                LOG.info("Parent:   {}", conflictRoot.canonicalGet(parentShape));
                LOG.info("Conflict: {}", conflictRoot);
                if (!conflictRoot.isUntouched()) {
                    conflicts.add(changedShape);
                }
            }

            if (branchDifferences.getRemovals().contains(changedShape)) {
                LOG.warn("Conflict. Parent #{} changed and branch #{} removed {}",
                    branch.getId(),
                    parent.getId(),
                    changedShape);
                LOG.info("Differences are: {}", branchRoot);

                conflicts.add(changedShape);
            }
        }

        LOG.info("Differences: {}", branchDifferences);
        LOG.info("Conflicts:   {}", conflicts);

        if (conflicts.isEmpty()) {
            LOG.info("No merge conflicts.");
            LOG.info("Adding new shapes.");
            for (BaseShape addedShape : branchDifferences.getAdditions()) {
                whiteboardDao.addShapeToWhiteboard(addedShape, parent);
            }

            LOG.info("Changing shapes.");
            for (BaseShape changedShape : branchDifferences.getChanges()) {
                whiteboardDao.updateShapeOnWhiteboard(changedShape, parent);
            }

            LOG.info("Removing shapes.");
            for (BaseShape removedShape : branchDifferences.getRemovals()) {
                whiteboardDao.removeShapeFromWhiteboard(removedShape, parent);
            }
        }

        return conflicts;
    }

    @Override
    public DiffNode findDifferences(int sourceId, int destinationId) {
        final Whiteboard source = getById(sourceId);
        final Whiteboard destination = getById(destinationId);

        DiffNode root = ObjectDifferBuilder.startBuilding()
            .introspection()
            .setDefaultIntrospector(new ShapeIntrospector())
            .and().build().compare(destination, source);
        LOG.info("Differences are: {}", root);

        final List<DiffNode> changes = new ArrayList<>();
        root.visit(new DiffNode.Visitor() {
            public void node(DiffNode node, Visit visit) {
                LOG.info("{} => {}", node.getPath(), node.getState());
                if (node.getPath().toString().startsWith("/shapes[")) {
                    final Object baseValue = node.canonicalGet(source);
                    final Object workingValue = node.canonicalGet(destination);
                    final String message = node.getPath() + " changed from " +
                        baseValue + " to " + workingValue;
                    LOG.info(message);
                    changes.add(node);
                    visit.dontGoDeeper();
                }
            }
        });
        LOG.info("Changes: {}", changes);
        return root;
    }

    @Override
    public void removeShape(BaseShape shape) {
        whiteboardDao.removeShape(shape);
    }

    @Override
    public void removeShapeFromWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        whiteboardDao.removeShapeFromWhiteboard(shape, whiteboard);

    }

    @Override
    public void updateShape(BaseShape shape) {
        whiteboardDao.updateShape(shape);
    }

    @Override
    public void updateShapeOnWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        whiteboardDao.updateShapeOnWhiteboard(shape, whiteboard);
    }
}
