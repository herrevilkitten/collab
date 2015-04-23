package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class TextShape extends BaseShape {
    private String text;

    private Point position;
}