package org.evilkitten.collab.application.entity;

import lombok.Data;

@Data
public class WhiteboardRectangle extends WhiteboardObject {
    private Point start = new Point();
}
