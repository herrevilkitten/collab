package org.evilkitten.collab.application.services.atmosphere.message;

import lombok.Data;

@Data
public class LineActionMessage extends ShapeActionMessage {
    private Point start;

    private Point end;
}
