package com.gym.members.verifit.base;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// JUnit-Jupiter extension which automatically starts and stops the containers that are used in the tests.
@Testcontainers
// A class-level annotation that is used to declare which active bean definition profiles should be used when loading an ApplicationContext for test classes.

// Tells Spring not to replace the application default DataSource.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Defines class-level metadata that is used to determine how to load and configure an ApplicationContext for integration tests.
@ContextConfiguration(initializers = BaseRepositoryTest.DataSourceInitializer.class)
public abstract class BaseRepositoryTest {

    // Annotation used in conjunction with the Testcontainers annotation to mark containers that should be managed by the Testcontainers extension.
    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:15.2");

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        /*
           `initialize` function allows us to set properties dynamically. Since the DataSource is initialized dynamically,
            we need to set url, username, and password that is provided/set by the testcontainers.
         */
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.test.database.replace=none", // Tells Spring Boot not to start in-memory db for tests.
                "spring.datasource.url=" + database.getJdbcUrl(),
                "spring.datasource.username=" + database.getUsername(),
                "spring.datasource.password=" + database.getPassword()
            );
        }
    }
}