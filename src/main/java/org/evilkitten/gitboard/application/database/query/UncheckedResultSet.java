package org.evilkitten.gitboard.application.database.query;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class UncheckedResultSet implements ResultSet {
    private final ResultSet resultSet;

    public UncheckedResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public boolean wasNull() {
        try {
            return resultSet.wasNull();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public String getString(int columnIndex) {
        try {
            return resultSet.getString(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean getBoolean(int columnIndex) {
        try {
            return resultSet.getBoolean(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public byte getByte(int columnIndex) {
        try {
            return resultSet.getByte(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public short getShort(int columnIndex) {
        try {
            return resultSet.getShort(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return resultSet.getInt(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return resultSet.getLong(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return resultSet.getFloat(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return resultSet.getDouble(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) {
        try {
            return resultSet.getBigDecimal(columnIndex, scale);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        try {
            return resultSet.getBytes(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Date getDate(int columnIndex) {
        try {
            return resultSet.getDate(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Time getTime(int columnIndex) {
        try {
            return resultSet.getTime(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) {
        try {
            return resultSet.getTimestamp(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) {
        try {
            return resultSet.getAsciiStream(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) {
        try {
            return resultSet.getUnicodeStream(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) {
        try {
            return resultSet.getBinaryStream(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public String getString(String columnLabel) {
        try {
            return resultSet.getString(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean getBoolean(String columnLabel) {
        try {
            return resultSet.getBoolean(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public byte getByte(String columnLabel) {
        try {
            return resultSet.getByte(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public short getShort(String columnLabel) {
        try {
            return resultSet.getShort(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getInt(String columnLabel) {
        try {
            return resultSet.getInt(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public long getLong(String columnLabel) {
        try {
            return resultSet.getLong(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public float getFloat(String columnLabel) {
        try {
            return resultSet.getFloat(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public double getDouble(String columnLabel) {
        try {
            return resultSet.getDouble(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) {
        try {
            return resultSet.getBigDecimal(columnLabel, scale);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public byte[] getBytes(String columnLabel) {
        try {
            return resultSet.getBytes(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Date getDate(String columnLabel) {
        try {
            return resultSet.getDate(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Time getTime(String columnLabel) {
        try {
            return resultSet.getTime(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) {
        try {
            return resultSet.getTimestamp(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) {
        try {
            return resultSet.getAsciiStream(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) {
        try {
            return resultSet.getUnicodeStream(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) {
        try {
            return resultSet.getBinaryStream(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public SQLWarning getWarnings() {
        try {
            return resultSet.getWarnings();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void clearWarnings() {
        try {
            resultSet.clearWarnings();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public String getCursorName() {
        try {
            return resultSet.getCursorName();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() {
        try {
            return resultSet.getMetaData();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Object getObject(int columnIndex) {
        try {
            return resultSet.getObject(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Object getObject(String columnLabel) {
        try {
            return resultSet.getObject(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int findColumn(String columnLabel) {
        try {
            return resultSet.findColumn(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Reader getCharacterStream(int columnIndex) {
        try {
            return resultSet.getCharacterStream(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Reader getCharacterStream(String columnLabel) {
        try {
            return resultSet.getCharacterStream(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return resultSet.getBigDecimal(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        try {
            return resultSet.getBigDecimal(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isBeforeFirst() {
        try {
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isAfterLast() {
        try {
            return resultSet.isAfterLast();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isFirst() {
        try {
            return resultSet.isFirst();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isLast() {
        try {
            return resultSet.isLast();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void beforeFirst() {
        try {
            resultSet.beforeFirst();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void afterLast() {
        try {
            resultSet.afterLast();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean first() {
        try {
            return resultSet.first();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean last() {
        try {
            return resultSet.last();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getRow() {
        try {
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean absolute(int row) {
        try {
            return resultSet.absolute(row);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean relative(int rows) {
        try {
            return resultSet.relative(rows);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean previous() {
        try {
            return resultSet.previous();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void setFetchDirection(int direction) {
        try {
            resultSet.setFetchDirection(direction);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getFetchDirection() {
        try {
            return resultSet.getFetchDirection();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void setFetchSize(int rows) {
        try {
            resultSet.setFetchSize(rows);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getFetchSize() {
        try {
            return resultSet.getFetchSize();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getType() {
        try {
            return resultSet.getType();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getConcurrency() {
        try {
            return resultSet.getConcurrency();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean rowUpdated() {
        try {
            return resultSet.rowUpdated();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean rowInserted() {
        try {
            return resultSet.rowInserted();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean rowDeleted() {
        try {
            return resultSet.rowDeleted();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNull(int columnIndex) {
        try {
            resultSet.updateNull(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) {
        try {
            resultSet.updateBoolean(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateByte(int columnIndex, byte x) {
        try {
            resultSet.updateByte(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateShort(int columnIndex, short x) {
        try {
            resultSet.updateShort(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateInt(int columnIndex, int x) {
        try {
            resultSet.updateInt(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateLong(int columnIndex, long x) {
        try {
            resultSet.updateLong(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateFloat(int columnIndex, float x) {
        try {
            resultSet.updateFloat(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateDouble(int columnIndex, double x) {
        try {
            resultSet.updateDouble(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) {
        try {
            resultSet.updateBigDecimal(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateString(int columnIndex, String x) {
        try {
            resultSet.updateString(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) {
        try {
            resultSet.updateBytes(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateDate(int columnIndex, Date x) {
        try {
            resultSet.updateDate(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateTime(int columnIndex, Time x) {
        try {
            resultSet.updateTime(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) {
        try {
            resultSet.updateTimestamp(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) {
        try {
            resultSet.updateAsciiStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) {
        try {
            resultSet.updateBinaryStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) {
        try {
            resultSet.updateCharacterStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) {
        try {
            resultSet.updateObject(columnIndex, x, scaleOrLength);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateObject(int columnIndex, Object x) {
        try {
            resultSet.updateObject(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNull(String columnLabel) {
        try {
            resultSet.updateNull(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) {
        try {
            resultSet.updateBoolean(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateByte(String columnLabel, byte x) {
        try {
            resultSet.updateByte(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateShort(String columnLabel, short x) {
        try {
            resultSet.updateShort(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateInt(String columnLabel, int x) {
        try {
            resultSet.updateInt(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateLong(String columnLabel, long x) {
        try {
            resultSet.updateLong(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateFloat(String columnLabel, float x) {
        try {
            resultSet.updateFloat(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateDouble(String columnLabel, double x) {
        try {
            resultSet.updateDouble(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) {
        try {
            resultSet.updateBigDecimal(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateString(String columnLabel, String x) {
        try {
            resultSet.updateString(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) {
        try {
            resultSet.updateBytes(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateDate(String columnLabel, Date x) {
        try {
            resultSet.updateDate(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateTime(String columnLabel, Time x) {
        try {
            resultSet.updateTime(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) {
        try {
            resultSet.updateTimestamp(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) {
        try {
            resultSet.updateAsciiStream(columnLabel, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) {
        try {
            resultSet.updateBinaryStream(columnLabel, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) {
        try {
            resultSet.updateCharacterStream(columnLabel, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) {
        try {
            resultSet.updateObject(columnLabel, x, scaleOrLength);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateObject(String columnLabel, Object x) {
        try {
            resultSet.updateObject(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void insertRow() {
        try {
            resultSet.insertRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateRow() {
        try {
            resultSet.updateRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void deleteRow() {
        try {
            resultSet.deleteRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void refreshRow() {
        try {
            resultSet.refreshRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void cancelRowUpdates() {
        try {
            resultSet.cancelRowUpdates();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void moveToInsertRow() {
        try {
            resultSet.moveToInsertRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void moveToCurrentRow() {
        try {
            resultSet.moveToCurrentRow();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Statement getStatement() {
        try {
            return resultSet.getStatement();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        try {
            return resultSet.getObject(columnIndex, map);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Ref getRef(int columnIndex) {
        try {
            return resultSet.getRef(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Blob getBlob(int columnIndex) {
        try {
            return resultSet.getBlob(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Clob getClob(int columnIndex) {
        try {
            return resultSet.getClob(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Array getArray(int columnIndex) {
        try {
            return resultSet.getArray(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) {
        try {
            return resultSet.getObject(columnLabel, map);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Ref getRef(String columnLabel) {
        try {
            return resultSet.getRef(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Blob getBlob(String columnLabel) {
        try {
            return resultSet.getBlob(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Clob getClob(String columnLabel) {
        try {
            return resultSet.getClob(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Array getArray(String columnLabel) {
        try {
            return resultSet.getArray(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) {
        try {
            return resultSet.getDate(columnIndex, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) {
        try {
            return resultSet.getDate(columnLabel, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) {
        try {
            return resultSet.getTime(columnIndex, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) {
        try {
            return resultSet.getTime(columnLabel, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) {
        try {
            return resultSet.getTimestamp(columnIndex, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) {
        try {
            return resultSet.getTimestamp(columnLabel, cal);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    public java.util.Date getJavaDate(int columnIndex) {
        Timestamp timestamp = getTimestamp(columnIndex);
        if (timestamp == null) {
            return null;
        }
        return new java.util.Date(timestamp.getTime());
    }

    public java.util.Date getJavaDate(String columnLabel) {
        Timestamp timestamp = getTimestamp(columnLabel);
        if (timestamp == null) {
            return null;
        }
        return new java.util.Date(timestamp.getTime());
    }


    @Override
    public URL getURL(int columnIndex) {
        try {
            return resultSet.getURL(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public URL getURL(String columnLabel) {
        try {
            return resultSet.getURL(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateRef(int columnIndex, Ref x) {
        try {
            resultSet.updateRef(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateRef(String columnLabel, Ref x) {
        try {
            resultSet.updateRef(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) {
        try {
            resultSet.updateBlob(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) {
        try {
            resultSet.updateBlob(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(int columnIndex, Clob x) {
        try {
            resultSet.updateClob(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(String columnLabel, Clob x) {
        try {
            resultSet.updateClob(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateArray(int columnIndex, Array x) {
        try {
            resultSet.updateArray(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateArray(String columnLabel, Array x) {
        try {
            resultSet.updateArray(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public RowId getRowId(int columnIndex) {
        try {
            return resultSet.getRowId(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public RowId getRowId(String columnLabel) {
        try {
            return resultSet.getRowId(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) {
        try {
            resultSet.updateRowId(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }

    }

    @Override
    public void updateRowId(String columnLabel, RowId x) {
        try {
            resultSet.updateRowId(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public int getHoldability() {
        try {
            return resultSet.getHoldability();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return resultSet.isClosed();
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNString(int columnIndex, String nString) {
        try {
            resultSet.updateNString(columnIndex, nString);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNString(String columnLabel, String nString) {
        try {
            resultSet.updateNString(columnLabel, nString);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) {
        try {
            resultSet.updateNClob(columnIndex, nClob);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) {
        try {
            resultSet.updateNClob(columnLabel, nClob);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public NClob getNClob(int columnIndex) {
        try {
            return resultSet.getNClob(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public NClob getNClob(String columnLabel) {
        try {
            return resultSet.getNClob(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) {
        try {
            return resultSet.getSQLXML(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) {
        try {
            return resultSet.getSQLXML(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) {
        try {
            resultSet.updateSQLXML(columnIndex, xmlObject);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) {
        try {
            resultSet.updateSQLXML(columnLabel, xmlObject);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public String getNString(int columnIndex) {
        try {
            return resultSet.getNString(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public String getNString(String columnLabel) {
        try {
            return resultSet.getNString(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) {
        try {
            return resultSet.getNCharacterStream(columnIndex);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) {
        try {
            return resultSet.getNCharacterStream(columnLabel);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) {
        try {
            resultSet.updateNCharacterStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
        try {
            resultSet.updateNCharacterStream(columnLabel, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) {
        try {
            resultSet.updateAsciiStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) {
        try {
            resultSet.updateBinaryStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) {
        try {
            resultSet.updateCharacterStream(columnIndex, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) {
        try {
            resultSet.updateAsciiStream(columnLabel, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) {
        try {
            resultSet.updateBinaryStream(columnLabel, x, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) {
        try {
            resultSet.updateCharacterStream(columnLabel, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) {
        try {
            resultSet.updateBlob(columnIndex, inputStream, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) {
        try {
            resultSet.updateBlob(columnLabel, inputStream, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) {
        try {
            resultSet.updateClob(columnIndex, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) {
        try {
            resultSet.updateClob(columnLabel, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) {
        try {
            resultSet.updateNClob(columnIndex, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) {
        try {
            resultSet.updateNClob(columnLabel, reader, length);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) {
        try {
            resultSet.updateNCharacterStream(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) {
        try {
            resultSet.updateNCharacterStream(columnLabel, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) {
        try {
            resultSet.updateAsciiStream(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) {
        try {
            resultSet.updateBinaryStream(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) {
        try {
            resultSet.updateCharacterStream(columnIndex, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) {
        try {
            resultSet.updateAsciiStream(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) {
        try {
            resultSet.updateBinaryStream(columnLabel, x);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) {
        try {
            resultSet.updateCharacterStream(columnLabel, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) {
        try {
            resultSet.updateBlob(columnIndex, inputStream);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) {
        try {
            resultSet.updateBlob(columnLabel, inputStream);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) {
        try {
            resultSet.updateClob(columnIndex, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) {
        try {
            resultSet.updateClob(columnLabel, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) {
        try {
            resultSet.updateNClob(columnIndex, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) {
        try {
            resultSet.updateNClob(columnLabel, reader);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) {
        try {
            return resultSet.getObject(columnIndex, type);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) {
        try {
            return resultSet.getObject(columnLabel, type);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        try {
            return resultSet.unwrap(iface);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        try {
            return resultSet.isWrapperFor(iface);
        } catch (SQLException e) {
            throw new StatementException(e);
        }
    }
}
