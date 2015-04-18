package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RectangleShape extends BaseShape {
    private String fill;

    private Point position;

    private Dimensions dimensions;
}
