package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;

@Data
public class EllipseShape extends BaseShape {
    private String fill;

    private Point position;

    private Dimensions dimensions;
}
