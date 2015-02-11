package org.pixi.collab.server.services.atmosphere.message;

import lombok.Data;

@Data
public class ShapeActionMessage extends ActionMessage {
    private String stroke;

    @Data
    public static class Point {
        float x;

        float y;
    }

    @Data
    public static class Dimensions {
        float height;

        float width;
    }
}
