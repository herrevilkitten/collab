package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RectangleActionMessage extends ShapeActionMessage {
    private String fill;

    private Point position;

    private Dimensions dimensions;
}
