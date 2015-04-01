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
import org.evilkitten.gitboard.application.services.atmosphere.message.ShapeActionMessage;
import org.evilkitten.gitboard.application.services.user.UserService;

public class PostgresqlWhiteboardDao implements WhiteboardDao {
    private final Config config;
    private final DataSource dataSource;
    private final UserService userService;

    @Inject
    public PostgresqlWhiteboardDao(Config config, DataSource dataSource, UserService userService) {
        this.config = config;
        this.dataSource = dataSource;
        this.userService = userService;
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

    public void addShapeToWhiteboard(ShapeActionMessage shape, Whiteboard whiteboard) {

    }
}
