package com.aqa.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.qameta.allure.Step;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public final class WireMockClient {

    private WireMockClient() {
    }

    @Step("Ответ на API запрос авторизации будет 200")
    public static void stubAuthSuccess(String token) {
        stubFor(post(urlEqualTo("/auth"))
                .withRequestBody(equalTo("token=" + token))
                .willReturn(aResponse()
                        .withStatus(200)));
    }

    @Step("Ответ на API запрос авторизации будет 400")
    public static void stubAuthFail(String token) {
        stubFor(post(urlEqualTo("/auth"))
                .withRequestBody(equalTo("token=" + token))
                .willReturn(aResponse()
                        .withStatus(400)));
    }

    @Step("Ответ на API запрос действие будет 200")
    public static void stubDoActionSuccess(String token) {
        stubFor(post(urlEqualTo("/doAction"))
                .withRequestBody(equalTo("token=" + token))
                .willReturn(aResponse()
                        .withStatus(200)));
    }

    @Step("Ответ на API запрос действие будет 400")
    public static void stubDoActionFail(String token) {
        stubFor(post(urlEqualTo("/doAction"))
                .withRequestBody(equalTo("token=" + token))
                .willReturn(aResponse()
                        .withStatus(400)));
    }

    @Step("Ответ на API запрос когда внешний сервис возвращает ошибку 500")
    public static void stubAuthServerError(String token) {
        stubFor(post(urlEqualTo("/auth"))
                .withRequestBody(equalTo("token=" + token))
                .willReturn(aResponse().withStatus(500)));
    }

    public static void setupFallbackStubs() {
        stubFor(post(urlMatching("/.*")).atPriority(5)
                .willReturn(aResponse().withStatus(400)));
    }

    public static void resetAll() {
        WireMock.reset();
    }
}
