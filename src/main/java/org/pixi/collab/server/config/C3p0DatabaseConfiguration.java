package org.pixi.collab.server.config;

import javax.inject.Singleton;
import javax.sql.DataSource;

import java.beans.PropertyVetoException;

import com.google.inject.Inject;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.typesafe.config.Config;

/**
 *
 */
@Singleton
public class C3p0DatabaseConfiguration implements DatabaseConfiguration {
    private static final String DATABASE_CLASS_KEY = "collab.database.class";
    private static final String DATABASE_URL_KEY = "collab.database.url";
    private static final String DATABASE_USERNAME_KEY = "collab.database.username";
    private static final String DATABASE_PASSWORD_KEY = "collab.database.password";

    private ComboPooledDataSource comboPooledDataSource;
    private Config config;

    /**
     * @param config the application configuration
     * @throws PropertyVetoException if the connection pool cannot be created
     */
    @Inject
    public C3p0DatabaseConfiguration(Config config) throws PropertyVetoException {
        this.config = config;
        this.createDataSource();
    }

    private void createDataSource() throws PropertyVetoException {
        this.comboPooledDataSource = new ComboPooledDataSource();
        this.comboPooledDataSource.setDriverClass(config.getString(DATABASE_CLASS_KEY));
        this.comboPooledDataSource.setJdbcUrl(config.getString(DATABASE_URL_KEY));
        this.comboPooledDataSource.setUser(config.getString(DATABASE_USERNAME_KEY));
        this.comboPooledDataSource.setPassword(config.getString(DATABASE_PASSWORD_KEY));
    }

    public DataSource getDataSource() {
        return this.comboPooledDataSource;
    }
}
