package com.aqa.tests;

import com.aqa.clients.WireMockClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static com.aqa.clients.WireMockClient.setupFallbackStubs;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public abstract class BaseTest {
    protected static WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8888));
        WireMock.configureFor("localhost", 8888);
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @AfterEach
    void afterEach() {
        WireMockClient.resetAll();
        setupFallbackStubs();
    }
}
