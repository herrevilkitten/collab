package org.evilkitten.gitboard.application.services.whiteboard.shape;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PathShape extends BaseShape {
    private final List<Segment> segments = new ArrayList<>();

    @Data
    public static class Segment {
        String type;

        Point position;
    }
}
