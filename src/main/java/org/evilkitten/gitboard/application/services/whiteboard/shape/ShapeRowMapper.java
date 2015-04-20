package org.evilkitten.gitboard.application.services.whiteboard.shape;

import java.sql.SQLException;

import org.evilkitten.gitboard.application.database.query.RowMapper;
import org.evilkitten.gitboard.application.database.query.UncheckedResultSet;
import org.evilkitten.gitboard.application.services.json.JsonDecoder;

public class ShapeRowMapper implements RowMapper<BaseShape> {
    private final JsonDecoder decoder;

    public ShapeRowMapper(JsonDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public BaseShape mapRow(UncheckedResultSet resultSet) throws SQLException {
        BaseShape shape = (BaseShape) decoder.fromJson(resultSet.getString("json"), BaseShape.class);
        shape.setId(resultSet.getInt("id"));
        shape.setCreationTime(resultSet.getJavaDate("creation_time"));
        shape.setBoardShapeId(resultSet.getInt("board_shape_id"));
        return shape;
    }
}
