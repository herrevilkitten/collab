package org.evilkitten.gitboard.application.entity;

import lombok.Data;

@Data
public class WhiteboardRectangle extends WhiteboardObject {
    private Point start = new Point();
}
