package org.pixi.common.database;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SqlQuery {
    private StringBuilder query = new StringBuilder();
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * @param lines
     * @return
     */
    public SqlQuery append(String... lines) {
        for (String line : lines) {
            query.append(line);
        }
        return this;
    }

    /**
     * @param lines
     * @return
     */
    public SqlQuery appendln(String... lines) {
        for (String line : lines) {
            query.append(line);
            query.append("\n");
        }
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public SqlQuery set(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    /**
     * @param dataSource
     * @param mapper
     * @param <T>
     * @return
     */
    public <T> T queryForObject(DataSource dataSource, RowMapper<T> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = finalizeQuery(connection);
            ResultSet rs = statement.executeQuery();

            T object = null;
            if (rs.next()) {
                object = mapper.mapRow(rs);
            }

            if (rs.next()) {
                throw new SqlQueryException("More than one row in ResultSet");
            }

            return object;
        } catch (SQLException e) {
            throw new SqlQueryException(e);
        }
    }

    /**
     * @param dataSource
     * @return
     */
    public boolean executeQuery(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = finalizeQuery(connection);
            boolean retval = statement.execute();
            connection.commit();
            return retval;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PreparedStatement finalizeQuery(Connection connection) throws SQLException {
        List<Object> parameterList = new ArrayList<>();

        String queryString = query.toString();
        Pattern p = Pattern.compile("\\?(\\w+)\\?");
        Matcher m = p.matcher(queryString);
        while (m.find()) {
            String key = m.group(1);
            if (!parameters.containsKey(key)) {
                throw new SqlQueryException(String.format("No key found for parameter ?%s?", key));
            }
            Object value = parameters.get(key);
            parameterList.add(value);
        }
        String finalQuery = m.replaceAll("?");
        PreparedStatement statement = connection.prepareStatement(finalQuery);
        for (int i = 0; i < parameterList.size(); ++i) {
            statement.setObject(i + 1, parameterList.get(i));
        }
        return statement;
    }
}
