package org.evilkitten.gitboard.application.database.query;

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

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Statement {
    private static final Logger LOG = LoggerFactory.getLogger(Statement.class);

    private final StringBuilder statementBuilder;
    private final Map<String, Object> statementParameters;
    private final List<Object> preparedParameters;

    public Statement() {
        statementBuilder = new StringBuilder();
        statementParameters = new HashMap<>();
        preparedParameters = new ArrayList<>();
    }

    public Statement(String sql) {
        this();
        append(sql);
    }

    public Statement append(String fragment) {
        statementBuilder.append(fragment);
        return this;
    }

    public Statement appendln(String fragment) {
        return this
            .append(fragment)
            .append(System.getProperty("line.separator"));
    }

    public Statement append(String... fragments) {
        for (String fragment : fragments) {
            append(fragment);
        }
        return this;
    }

    public Statement appendln(String... fragments) {
        for (String fragment : fragments) {
            this.appendln(fragment);
        }
        return this;
    }

    public Statement set(String parameter, Object value) {
        statementParameters.put(parameter, value);
        return this;
    }

    public Statement set(int parameter, Object value) {
        statementParameters.put(Integer.toString(parameter), value);
        return this;
    }

    private final static Pattern PARAMETER_PATTERN = Pattern.compile(":(\\w+)");

    public String prepareStatement() {
        StringBuilder preparedStatement = new StringBuilder(statementBuilder.toString());
        StringBuilder loggingStatement = new StringBuilder(statementBuilder.toString());

        preparedParameters.clear();
        while (true) {
            Matcher matcher = PARAMETER_PATTERN.matcher(preparedStatement);
            if (!matcher.find()) {
                break;
            }

            int start = matcher.start();
            int end = matcher.end();
            String parameterName = matcher.group(1);
            if (!statementParameters.containsKey(parameterName)) {
                throw new IllegalArgumentException("No query parameter matching '" + parameterName + "'");
            }
            Object parameterValue = statementParameters.get(parameterName);
            preparedStatement.replace(start, end, "?");
            if (LOG.isDebugEnabled()) {
                Matcher loggingMatcher = PARAMETER_PATTERN.matcher(loggingStatement);
                if (loggingMatcher.find()) {
                    int loggingStart = loggingMatcher.start();
                    int loggingEnd = loggingMatcher.end();
                    loggingStatement.replace(loggingStart, loggingEnd, "?" + parameterValue + "?");
                }
            }
            preparedParameters.add(parameterValue);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Prepared statement: {}", loggingStatement.toString());
        }

        return preparedStatement.toString();
    }

    private ResultSet executeQuery(Connection connection) throws SQLException {
        try {
            String sql = prepareStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int parameterIndex = 1;
            for (Object parameter : preparedParameters) {
                preparedStatement.setObject(parameterIndex++, parameter);

            }

            return preparedStatement.executeQuery();
        } catch (Exception e) {
            LOG.error("Exception while executing statement: {}", statementBuilder.toString());
            LOG.error("{}", e);
            throw e;
        }
    }

    private int executeUpdate(Connection connection) throws SQLException {
        try {
            String sql = prepareStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int parameterIndex = 1;
            for (Object parameter : preparedParameters) {
                preparedStatement.setObject(parameterIndex++, parameter);

            }

            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception while executing statement: {}", statementBuilder.toString());
            LOG.error("{}", e);
            throw e;
        }
    }

    private UncheckedResultSet executeUpdateAndReturn(Connection connection, String... keys) throws SQLException {
        try {
            String sql = prepareStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, keys);

            int parameterIndex = 1;
            for (Object parameter : preparedParameters) {
                preparedStatement.setObject(parameterIndex++, parameter);

            }

            preparedStatement.executeUpdate();
            return new UncheckedResultSet(preparedStatement.getGeneratedKeys());
        } catch (Exception e) {
            LOG.error("Exception while executing statement: {}", statementBuilder.toString());
            LOG.error("{}", e);
            throw e;
        }
    }

    public int update(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return executeUpdate(connection);
        } catch (SQLException exception) {
            throw new StatementException(exception);
        }
    }

    public UncheckedResultSet updateAndReturn(Connection connection, String... columns) {
        try {
            return executeUpdateAndReturn(connection, columns);
        } catch (SQLException exception) {
            throw new StatementException(exception);
        }
    }

    public <T> List<T> queryForRows(DataSource dataSource, RowMapper<? extends T> rowMapper) {
        try (Connection connection = dataSource.getConnection()) {

            UncheckedResultSet resultSet = new UncheckedResultSet(executeQuery(connection));
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet));
            }
            return results;
        } catch (SQLException exception) {
            throw new StatementException(exception);
        }
    }

    public <T> T queryForRow(DataSource dataSource, RowMapper<? extends T> rowMapper) {
        List<T> results = queryForRows(dataSource, rowMapper);
        if (results.isEmpty()) {
            throw new RecordNotFoundException();
        }
        if (results.size() > 1) {
            throw new TooManyRecordsException();
        }
        return results.get(0);
    }

    public Integer queryForInteger(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = executeQuery(connection);
            if (!resultSet.next()) {
                throw new RecordNotFoundException();
            }
            return resultSet.getInt(1);
        } catch (SQLException exception) {
            throw new StatementException(exception);
        }
    }
}
