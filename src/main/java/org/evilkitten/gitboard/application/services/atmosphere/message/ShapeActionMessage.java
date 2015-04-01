package org.evilkitten.gitboard.application.services.atmosphere.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
abstract public class ShapeActionMessage extends ActionMessage {
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
