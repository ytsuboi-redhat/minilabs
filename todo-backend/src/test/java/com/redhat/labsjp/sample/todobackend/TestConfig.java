package com.redhat.labsjp.sample.todobackend;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Autowired
    DataSource dataSource;

    @Bean("mysqlDatabaseConnection")
    public IDatabaseConnection dbUnitDatabaseConnection(DataSource dataSource) throws DatabaseUnitException, SQLException {
        return new MySqlConnection(dataSource.getConnection(), "todo");
    }
}