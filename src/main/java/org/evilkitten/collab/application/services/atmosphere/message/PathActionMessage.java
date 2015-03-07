package org.evilkitten.collab.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PathActionMessage extends ShapeActionMessage {
    private final List<Segment> segments = new ArrayList<>();

    @Data
    public static class Segment {
        String type;

        Point position;
    }
}
