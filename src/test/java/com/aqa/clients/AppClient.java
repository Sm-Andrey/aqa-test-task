package com.aqa.clients;

import com.aqa.tests.LoginTests;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.aqa.config.TestConfig.API_KEY;
import static com.aqa.config.TestConfig.BASE_URL;
import static io.restassured.RestAssured.given;

public final class AppClient {

    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

    private AppClient() {
    }

    @Step("Отправляем API запрос с токеном и параметром действия")
    public static Response preparationRequest(String token, String action) {
        Response response = given()
                .header("X-Api-Key", API_KEY)
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .body("token=" + token + "&action=" + action)
                .when()
                .post(BASE_URL + "/endpoint");

        log.info("StatusCode: {}", response.getStatusCode());
        log.info("Message: {}", response.getBody().asString());
        return response;
    }

    @Step("Отправляем API запрос с токеном и параметром действия, но без ключа")
    public static Response preparationRequestWithoutApiKey(String token, String action) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .body("token=" + token + "&action=" + action)
                .when()
                .post(BASE_URL + "/endpoint");

        log.info("StatusCode: {}", response.getStatusCode());
        log.info("Message: {}", response.getBody().asString());
        return response;
    }

    @Step("Отправляем API запрос с токеном и параметром действия, но с методом GET вместо POST")
    public static Response preparationRequestWithGET(String token, String action) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .body("token=" + token + "&action=" + action)
                .when()
                .get(BASE_URL + "/endpoint");

        log.info("StatusCode: {}", response.getStatusCode());
        log.info("Message: {}", response.getBody().asString());
        return response;
    }
}
