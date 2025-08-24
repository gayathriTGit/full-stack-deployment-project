package com.example.springbootmvcjdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        // try to load from classpath
        ClassPathResource resource = new ClassPathResource("students_db.db");

        Path dbPath;
        if (resource.exists()) {
            // copy to a writable temp location (works inside jar + in Jenkins)
            dbPath = Files.createTempFile("students_db", ".db");
            try (InputStream in = resource.getInputStream()) {
                Files.copy(in, dbPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            // ensure it sticks around for the process lifetime
            dbPath.toFile().deleteOnExit();
        } else {
            // fallback to a stable filename in the working dir
            dbPath = Path.of("students_db.db");
            if (!Files.exists(dbPath)) {
                // create empty db if not present
                Files.createFile(dbPath);
            }
        }

        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();
        System.out.println("Using SQLite database at: " + url);

        org.sqlite.SQLiteDataSource ds = new org.sqlite.SQLiteDataSource();
        ds.setUrl(url);
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
