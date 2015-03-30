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
import org.evilkitten.gitboard.application.services.json.JsonEncoder;
import org.evilkitten.gitboard.application.services.user.UserService;
import org.evilkitten.gitboard.application.services.whiteboard.shape.BaseShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresqlWhiteboardDao implements WhiteboardDao {
    private static final Logger LOG = LoggerFactory.getLogger(PostgresqlWhiteboardDao.class);

    private final Config config;
    private final DataSource dataSource;
    private final UserService userService;
    private final JsonEncoder jsonEncoder;

    @Inject
    public PostgresqlWhiteboardDao(Config config,
                                   DataSource dataSource,
                                   UserService userService,
                                   JsonEncoder jsonEncoder) {
        this.config = config;
        this.dataSource = dataSource;
        this.userService = userService;
        this.jsonEncoder = jsonEncoder;
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
    public Whiteboard create(User creator) {
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.create"));
        statement.set("creator", creator.getId());

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
        Statement statement = new Statement(config.getString("gitboard.private.database.sql.whiteboard.addShapeToWhiteboard"));
        statement.set("board", whiteboard.getId());

        LOG.info("Shape is: {}", shape);
        statement.set("json", jsonEncoder.toJson(shape));

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
}
