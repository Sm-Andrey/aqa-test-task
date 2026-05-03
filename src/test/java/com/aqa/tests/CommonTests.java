package com.aqa.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.AppClient.preparationRequestWithGET;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.VALID_TOKEN;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;

@Epic("Дополнительные проверки.")
@Feature("COMMON")
public class CommonTests extends BaseTest {

    @Test
    @DisplayName("TC1: Запрос без обязательного параметра action.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система не обрабатывает запрос, если не указан тип действия (action).")
    public void testFailLoginWithoutAction() {

        step("Пользователь отправляет запрос, не указав, какое действие нужно выполнить.", () -> {
            preparationRequest(VALID_TOKEN, "")
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("TC2: Запрос без обязательного параметра token")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система не обрабатывает запрос, если не указан токен.")
    public void testFailLoginWithoutToken() {

        step("Пользователь отправляет запрос, не указав токен.", () -> {
            preparationRequest("", LOGIN)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("TC3: Запрос с неверным HTTP-методом.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система не обрабатывает запрос, если указан неверным HTTP-методом (GET вместо POST).")
    public void testFailLoginInvalidMethod() {

        step("Пользователь отправляет запрос с HTTP-методом GET.", () -> {
            preparationRequestWithGET(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(401)
                    .body("result", equalTo("ERROR"));
        });
    }
}
