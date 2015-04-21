package org.evilkitten.gitboard.application.services.whiteboard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.typesafe.config.Config;
import org.evilkitten.gitboard.application.database.query.Statement;
import org.evilkitten.gitboard.application.database.query.StatementException;
import org.evilkitten.gitboard.application.database.query.UncheckedResultSet;
import org.evilkitten.gitboard.application.entity.User;
import org.evilkitten.gitboard.application.services.json.JsonTranscoder;
import org.evilkitten.gitboard.application.services.user.UserService;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.evilkitten.gitboard.application.services.whiteboard.shape.ShapeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresqlWhiteboardDao implements WhiteboardDao {
    private static final Logger LOG = LoggerFactory.getLogger(PostgresqlWhiteboardDao.class);

    private final Config config;
    private final DataSource dataSource;
    private final UserService userService;
    private final JsonTranscoder jsonTranscoder;

    @Inject
    public PostgresqlWhiteboardDao(Config config,
                                   DataSource dataSource,
                                   UserService userService,
                                   JsonTranscoder jsonTranscoder) {
        this.config = config;
        this.dataSource = dataSource;
        this.userService = userService;
        this.jsonTranscoder = jsonTranscoder;
    }

    @Override
    public Whiteboard getById(Integer id) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.getById"));

        statement.set("id", id);
        return statement.queryForRow(dataSource, new WhiteboardRowMapper(userService));
    }

    @Override
    public List<Whiteboard> getAllByCreator(User creator) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.getAllByCreator"));

        statement.set("creatorId", creator.getId());
        return statement.queryForRows(dataSource, new WhiteboardRowMapper(userService));
    }

    @Override
    public List<Whiteboard> getAllByAccess(User user) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.getAllByAccess"));

        statement.set("userId", user.getId());
        return statement.queryForRows(dataSource, new WhiteboardRowMapper(userService));
    }

    @Override
    public Whiteboard create(User creator, String name, Integer parentId, Integer originalId) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.create"));
        statement.set("creator", creator.getId());
        statement.set("name", name);
        statement.set("parent", parentId);
        statement.set("original", originalId);

        try (Connection connection = dataSource.getConnection()) {
            UncheckedResultSet resultSet = statement.updateAndReturn(connection, "id");
            if (resultSet.next()) {
                return getById(resultSet.getInt("id"));
            } else {
                throw new StatementException("updateAndReturn did not return a ResultSet");
            }
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public BaseShape addShapeToWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        Statement statement;
        if (shape.getBoardShapeId() != null) {
            statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.addOldShapeToWhiteboard"));
            statement.set("boardShapeId", shape.getBoardShapeId());
        } else {
            statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.addShapeToWhiteboard"));
        }
        statement.set("board", whiteboard.getId());
        statement.set("json", jsonTranscoder.toJson(shape));

        try (Connection connection = dataSource.getConnection()) {
            UncheckedResultSet resultSet = statement.updateAndReturn(connection, "id");
            if (resultSet.next()) {
                shape.setId(resultSet.getInt("id"));
                return shape;
            } else {
                throw new StatementException("updateAndReturn did not return a ResultSet");
            }
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public List<BaseShape> getShapesForWhiteboard(Integer id) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.getShapesForWhiteboard"));
        statement.set("board", id);
        return statement.queryForRows(dataSource, new ShapeRowMapper(jsonTranscoder));
    }

    @Override
    public void removeShape(BaseShape shape) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.removeShape"));
        statement.set("id", shape.getId());
        statement.update(dataSource);
    }

    @Override
    public void removeShapeFromWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.removeShapeFromWhiteboard"));
        statement.set("boardId", whiteboard.getId());
        statement.set("boardShapeId", shape.getBoardShapeId());
        statement.update(dataSource);
    }

    @Override
    public void updateShape(BaseShape shape) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.updateShape"));
        statement.set("id", shape.getId());
        statement.set("json", jsonTranscoder.toJson(shape));
        statement.update(dataSource);
    }

    @Override
    public void updateShapeOnWhiteboard(BaseShape shape, Whiteboard whiteboard) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.updateShapeOnWhiteboard"));
        statement.set("boardId", whiteboard.getId());
        statement.set("boardShapeId", shape.getBoardShapeId());
        statement.set("json", jsonTranscoder.toJson(shape));
        statement.update(dataSource);
    }
}
