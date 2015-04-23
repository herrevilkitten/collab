package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddShapeMessage extends ActionMessage implements EchoedMessage {
    private BaseShape shape;
}
