package com.igmiller.booking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
public class DatabaseConnectionCheck implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionCheck.class);
    private final DataSource dataSource;

    public DatabaseConnectionCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            log.info("Database connection established: {} {} ({}) as {}", meta.getDatabaseProductName(), meta.getDatabaseProductVersion(), meta.getURL(), meta.getUserName());
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect to database", e);
        }
    }
}
