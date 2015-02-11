package org.pixi.collab.server.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WhiteboardPath extends WhiteboardObject {
    final List<Segment> segments = new ArrayList<>();

    @Data
    public class Segment {
        private char command = ' ';
        private Point position = new Point();
    }
}
