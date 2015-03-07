package org.evilkitten.collab.application.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WhiteboardPath extends WhiteboardObject {
    final List<Segment> segments = new ArrayList<>();

    @Data
    public class Segment {
        private char command = ' ';
        private Point position = new Point();
    }
}
