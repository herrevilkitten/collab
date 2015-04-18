package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;

@Data
public class LineShape extends BaseShape {
    private Point start;

    private Point end;
}
