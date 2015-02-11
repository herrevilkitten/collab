package org.pixi.collab.server.entity;

import lombok.Data;

@Data
public class WhiteboardLine extends WhiteboardObject {
    private Point start = new Point();
    private Point end = new Point();
}
