package org.evilkitten.gitboard.application.services.whiteboard.shape;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

public class RectangleShape extends BaseShape {
    private String fill;

    private Point position;

    private Dimensions dimensions;
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangleShape that = (RectangleShape) o;

        if (fill != null ? !fill.equals(that.fill) : that.fill != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        return !(dimensions != null ? !dimensions.equals(that.dimensions) : that.dimensions != null);

    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (fill != null ? fill.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (dimensions != null ? dimensions.hashCode() : 0);
        return result;
    }
    */
}
