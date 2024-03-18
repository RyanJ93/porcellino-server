package dev.enricosola.porcellino.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clean(){
        this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        this.jdbcTemplate.execute("TRUNCATE TABLE portfolios;");
        this.jdbcTemplate.execute("TRUNCATE TABLE users;");
        this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }
}
