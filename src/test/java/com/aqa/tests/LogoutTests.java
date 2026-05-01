package com.aqa.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.WireMockClient.stubAuthFail;
import static com.aqa.clients.WireMockClient.stubAuthSuccess;
import static com.aqa.data.TestData.ACTION;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.LOGOUT;
import static com.aqa.data.TestData.VALID_TOKEN;
import static org.hamcrest.Matchers.equalTo;

@Epic("Завершение сессии")
@Feature("LOGOUT")
public class LogoutTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешный выход для аутентифицированного токена")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessLogout() {

        stubAuthSuccess(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN)
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));

        preparationRequest(VALID_TOKEN, LOGOUT);

        preparationRequest(VALID_TOKEN, ACTION)
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC2: Попытка выхода для неаутентифицированного токена")
    @Severity(SeverityLevel.CRITICAL)
    public void testFailLogoutWithNotAuthorizedToken() {

        stubAuthFail(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGOUT);

        preparationRequest(VALID_TOKEN, ACTION)
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }
}
