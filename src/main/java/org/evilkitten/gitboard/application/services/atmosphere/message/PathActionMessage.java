package org.evilkitten.gitboard.application.services.atmosphere.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PathActionMessage extends ShapeActionMessage {
    private final List<Segment> segments = new ArrayList<>();

    @Override
    public String getType() {
        return "action.path";
    }

    @Data
    public static class Segment {
        String type;

        Point position;
    }
}
