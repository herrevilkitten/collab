package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;

@Data
public class LineActionMessage extends ShapeActionMessage {
    private Point start;

    private Point end;

    @Override
    public String getType() {
        return "action.line";
    }
}
