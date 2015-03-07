package org.evilkitten.collab.application.entity;

import java.util.Calendar;

import lombok.Data;

@Data
abstract public class WhiteboardObject {
    private Integer id;
    private User creator;
    private Calendar timestamp;

    private int strokeColor;
    private int strokeOpacity;
    private int fillColor;
    private int fillOpacity;
    private int strokeWidth;
    private int zIndex;

    @Data
    public class Point {
        double x;
        double y;

        public Point() {
            this(0.0, 0.0);
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    public class Dimensions {
        double width;
        double height;

        public Dimensions() {
            this(0.0, 0.0);
        }

        public Dimensions(double width, double height) {
            this.width = width;
            this.height = height;
        }
    }
}
