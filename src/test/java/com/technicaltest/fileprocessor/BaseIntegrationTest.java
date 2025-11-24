package com.technicaltest.fileprocessor;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.technicaltest.fileprocessor.stub.StubForIPAPI;

import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {

    protected static final WireMockServer wireMockServer;
    protected final StubForIPAPI stubForIPAPI;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static {
        wireMockServer = new WireMockServer(0); // dynamic port
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());
    }

    protected BaseIntegrationTest() {
        this.stubForIPAPI = new StubForIPAPI(wireMockServer);
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("wiremock.base-url", wireMockServer::baseUrl);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @AfterAll
    static void tearDownInfrastructure() {
    }
}
