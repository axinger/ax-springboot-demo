package com.github.axinger;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlywayH2IntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testMigrationsApplied() {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        MigrationInfoService info = flyway.info();

        System.out.println("info.applied() = " + Arrays.toString(info.applied()));
    }
}
