package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemoveShapeMessage extends ActionMessage {
    private int shapeId;
}
