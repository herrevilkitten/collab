package org.evilkitten.gitboard.application.database;

import javax.sql.DataSource;

public interface DataSourceProvider {
    public DataSource getDataSource();
}
