package org.evilkitten.gitboard.application.services.whiteboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.evilkitten.gitboard.application.services.whiteboard.shape.ShapeDifference;

@Data
public class WhiteboardDifferences {
    private final Set<BaseShape> additions = new HashSet<>();
    private final Set<BaseShape> removals = new HashSet<>();
    private final Set<BaseShape> changes = new HashSet<>();
    private final Map<BaseShape, ShapeDifference> shapes = new HashMap<>();
}
