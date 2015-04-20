package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;

@Data
public class ShapeDifference {
    private BaseShape original;
    private BaseShape parent;
    private BaseShape branch;
}
