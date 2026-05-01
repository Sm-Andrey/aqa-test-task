package com.aqa.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.WireMockClient.stubAuthSuccess;
import static com.aqa.clients.WireMockClient.stubDoActionFail;
import static com.aqa.clients.WireMockClient.stubDoActionSuccess;
import static com.aqa.data.TestData.ACTION;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.LOGOUT;
import static com.aqa.data.TestData.SHORT_TOKEN;
import static com.aqa.data.TestData.VALID_TOKEN;
import static org.hamcrest.Matchers.equalTo;

@Epic("Действие")
@Feature("ACTION")
public class ActionTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешное выполнение действия с предварительно аутентифицированным токеном")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessDoAction() {

        stubAuthSuccess(VALID_TOKEN);
        stubDoActionSuccess(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN);

        preparationRequest(VALID_TOKEN, ACTION)
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));

        preparationRequest(VALID_TOKEN, LOGOUT);
    }

    @Test
    @DisplayName("TC2: Попытка выполнить действие без предварительного LOGIN")
    @Severity(SeverityLevel.BLOCKER)
    public void testFailDoActionWithoutToken() {

        stubDoActionFail(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, ACTION)
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC3: Попытка выполнить действие после LOGOUT")
    @Severity(SeverityLevel.CRITICAL)
    public void testFailDoActionAfterLogout() {

        stubAuthSuccess(VALID_TOKEN);
        stubDoActionFail(VALID_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN);

        preparationRequest(VALID_TOKEN, LOGOUT);

        preparationRequest(VALID_TOKEN, ACTION)
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @DisplayName("TC4: Действие с невалидным токеном (короткий/спецсимволы)")
    @Severity(SeverityLevel.BLOCKER)
    public void testFailDoActionWithInvalidToken() {

        stubAuthSuccess(VALID_TOKEN);
        stubDoActionFail(SHORT_TOKEN);

        preparationRequest(VALID_TOKEN, LOGIN);

        preparationRequest(SHORT_TOKEN, ACTION)
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));

        preparationRequest(VALID_TOKEN, LOGOUT);
    }
}
