package com.evm.oauth2.testcontainers;

import org.testcontainers.containers.MySQLContainer;

public class SingletonMySQLContainer extends MySQLContainer<SingletonMySQLContainer> {

    private static final String IMAGE_VERSION = "mysql:8.2";
    private static SingletonMySQLContainer container;

    private SingletonMySQLContainer() {
        super(IMAGE_VERSION);
        withDatabaseName("test-db");
        withUsername("test");
        withPassword("test");
    }

    public static SingletonMySQLContainer getInstance() {
        if (container == null) {
            container = new SingletonMySQLContainer();
            container.start();
        }
        return container;
    }

    @Override
    public void stop() {
        // Override stop so that the container is not stopped between Spring contexts.
        // It will be stopped when the JVM shuts down.
    }
}