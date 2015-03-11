package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;

@Data
public class EllipseActionMessage extends ShapeActionMessage {
    private String fill;

    private Point position;

    private Dimensions dimensions;
}