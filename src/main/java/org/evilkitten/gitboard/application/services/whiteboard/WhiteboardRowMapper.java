package org.evilkitten.gitboard.application.services.whiteboard;

import java.sql.SQLException;

import org.evilkitten.gitboard.application.database.query.RowMapper;
import org.evilkitten.gitboard.application.database.query.UncheckedResultSet;
import org.evilkitten.gitboard.application.services.user.UserService;

public class WhiteboardRowMapper implements RowMapper<Whiteboard> {
    private UserService userService;

    public WhiteboardRowMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Whiteboard mapRow(UncheckedResultSet resultSet) throws SQLException {
        Whiteboard whiteboard = new Whiteboard();

        whiteboard.setId(resultSet.getInt("id"));
        String name = resultSet.getString("boardName");
        if (name == null || name.isEmpty()) {
            name = "Whiteboard #" + whiteboard.getId();
        }
        whiteboard.setName(name);
        whiteboard.setCreator(userService.getById(resultSet.getInt("creatorId")));
        whiteboard.setCreationTime(resultSet.getJavaDate("creationTime"));

        Integer parentId = resultSet.getInt("parent");
        if (resultSet.wasNull()) {
            parentId = null;
        }
        whiteboard.setParentId(parentId);

        Integer originalId = resultSet.getInt("original");
        if (resultSet.wasNull()) {
            originalId = null;
        }
        whiteboard.setOriginalId(originalId);

        return whiteboard;
    }
}
