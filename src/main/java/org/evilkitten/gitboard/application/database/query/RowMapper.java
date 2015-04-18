package org.evilkitten.gitboard.application.database.query;

import java.sql.SQLException;

public interface RowMapper<T> {
    T mapRow(UncheckedResultSet resultSet) throws SQLException;
}
