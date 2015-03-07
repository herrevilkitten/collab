package org.evilkitten.collab.application.database;

import javax.sql.DataSource;

public interface DataSourceProvider {
    public DataSource getDataSource();
}
