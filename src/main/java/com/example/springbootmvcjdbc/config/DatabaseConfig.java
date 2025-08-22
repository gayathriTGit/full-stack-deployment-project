package com.example.springbootmvcjdbc.config;

import com.example.springbootmvcjdbc.SpringBootMvcJdbcApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    // Configure your database path here
    private static String DB_PATH = "" ; //"/Users/ritet/OneDrive/Documents/Coding/Caltech/FSD bootcamp/course 3/spring-boot-mvc-jdbc/src/main/resources/db/students_db.db";

    @Bean
    public DataSource dataSource() throws URISyntaxException {

//        try {
//            URL resourceUrl = SpringBootMvcJdbcApplication.class.getClassLoader().getResource("students_db.db");
//            Path resourcePath = Paths.get(resourceUrl.toURI());
//            System.out.println("Resource Path: " + resourcePath.toAbsolutePath());
//            DB_PATH = resourcePath.toAbsolutePath().toString();
//        }catch(URISyntaxException ex){
//            System.out.println(ex.getMessage());
//        }
        try {
            InputStream dbStream = SpringBootMvcJdbcApplication.class.getClassLoader().getResourceAsStream("students_db.db");
            File target = new File("students_db.db");
            System.out.println("Exists" + target.exists() + target.toPath().toString());
            if (!target.exists()) {
                Files.copy(dbStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            DB_PATH =target.toPath().toString();
            System.out.println("DB_PATH" + DB_PATH);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        org.sqlite.SQLiteDataSource dataSource = new org.sqlite.SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + DB_PATH);

        System.out.println("Using SQLite database at: " + DB_PATH);

        return dataSource;
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
