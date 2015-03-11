package org.evilkitten.gitboard.application.database;

import javax.inject.Singleton;
import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataSourceProvider.class).to(PooledDataSourceProvider.class).in(Singleton.class);
    }

    @Provides
    public DataSource provideDataSource(DataSourceProvider dataSourceProvider) {
        return dataSourceProvider.getDataSource();
    }
}
