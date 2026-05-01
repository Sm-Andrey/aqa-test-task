package com.aqa.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.AppClient.preparationRequestWithGET;
import static com.aqa.clients.WireMockClient.stubAuthSuccess;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.VALID_TOKEN;
import static org.hamcrest.Matchers.equalTo;

@Epic("Дополнительные проверки")
@Feature("COMMON")
public class CommonTests extends BaseTest {

    @Test
    @DisplayName("TC1: Запрос без обязательного параметра action")
    @Severity(SeverityLevel.BLOCKER)
    public void testFailLoginWithoutAction() {

        stubAuthSuccess(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, "")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC2: Запрос без обязательного параметра token")
    @Severity(SeverityLevel.BLOCKER)
    public void testFailLoginWithoutToken() {

        stubAuthSuccess(VALID_TOKEN);

        preparationRequest("", LOGIN)
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC3: Запрос с неверным HTTP-методом (GET вместо POST)")
    @Severity(SeverityLevel.BLOCKER)
    public void testFailLoginInvalidMethod() {

        stubAuthSuccess(VALID_TOKEN);

        preparationRequestWithGET(VALID_TOKEN, LOGIN)
                .then()
                .statusCode(401)
                .body("result", equalTo("ERROR"));
    }
}
