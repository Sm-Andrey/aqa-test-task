package com.aqa.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.AppClient.preparationRequestWithoutApiKey;
import static com.aqa.clients.WireMockClient.stubAuthFail;
import static com.aqa.clients.WireMockClient.stubAuthServerError;
import static com.aqa.clients.WireMockClient.stubAuthSuccess;
import static com.aqa.data.TestData.EMPTY_TOKEN;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.LOGOUT;
import static com.aqa.data.TestData.LONG_TOKEN;
import static com.aqa.data.TestData.SHORT_TOKEN;
import static com.aqa.data.TestData.TOKEN_WITH_SPECIAL;
import static com.aqa.data.TestData.VALID_TOKEN;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


@Epic("Аутентификация")
@Feature("LOGIN")
public class LoginTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешная аутентификация с валидным токеном")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessLogin() {

        stubAuthSuccess(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN)
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));

        preparationRequest(VALID_TOKEN, LOGOUT);
    }

    @Test
    @DisplayName("ТС2: Аунтификация с коротким логином")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithSnortToken() {

        stubAuthFail(SHORT_TOKEN);

        preparationRequest(SHORT_TOKEN, LOGIN)
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("ТС3: Аунтификация с длинным токеном")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithLongToken() {

        stubAuthFail(LONG_TOKEN);

        preparationRequest(LONG_TOKEN, LOGIN)
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("ТС4: Аунтификация с токеном со спецсимволами")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithSpecialToken() {

        stubAuthFail(TOKEN_WITH_SPECIAL);

        preparationRequest(TOKEN_WITH_SPECIAL, LOGIN)
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", containsString("token: должно соответствовать"));
    }


    @Test
    @DisplayName("TC5: Аутентификация с пустым токеном")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithEmptyToken() {

        stubAuthFail(EMPTY_TOKEN);

        preparationRequest(EMPTY_TOKEN, LOGIN)
                .then()
                .statusCode(400)
                .body("message", containsString("token: должно соответствовать"));
    }


    @Test
    @DisplayName("TC6: Аутентификация, когда внешний сервис возвращает ошибку")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithExternalServiceError() {

        stubAuthServerError(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN)
                .then()
                .statusCode(500)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC7: Аутентификация без API-ключа")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginWithoutApiKey() {

        preparationRequestWithoutApiKey(VALID_TOKEN, LOGIN)
                .then()
                .statusCode(401)
                .body("result", equalTo("ERROR"));
    }
}
