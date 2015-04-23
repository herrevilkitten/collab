package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LineShape extends BaseShape {
    private Point start;

    private Point end;
}
