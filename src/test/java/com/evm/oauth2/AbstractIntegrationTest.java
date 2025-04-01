package com.evm.oauth2;

import com.evm.oauth2.testcontainers.SingletonMySQLContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

//    @Container
//    protected static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
//            .withDatabaseName("test-db")
//            .withUsername("test")
//            .withPassword("test");

    static final SingletonMySQLContainer mysql = SingletonMySQLContainer.getInstance();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.driverClassName", mysql::getDriverClassName);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeAll
    static void checkContainer() {
        System.out.println("MySQL is running: " + mysql.isRunning());
    }

//    @BeforeAll
//    protected static void beforeAll() {
//        mysql.start();
//    }
//
//    @AfterAll
//    protected static void afterAll() {
//        mysql.stop();
//    }

}
