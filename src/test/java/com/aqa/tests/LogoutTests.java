package com.aqa.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.aqa.clients.AppClient.preparationRequest;
import static com.aqa.clients.WireMockClient.stubAuthSuccess;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.LOGOUT;
import static com.aqa.data.TestData.VALID_TOKEN;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;

@Epic("Завершение сессии.")
@Feature("LOGOUT")
public class LogoutTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешный выход для аутентифицированного токена.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Сессия успешно завершается, пользователь выходит из системы.")
    public void testSuccessLogout() {

        step("Подготовка: Регистрация пользователя во внешней системе с валидным ключом " + VALID_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthSuccess(VALID_TOKEN);
        });

        step("Пользователь успешно входит в систему.", () -> {
            preparationRequest(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь успешно выходит из системы.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });
    }

    @Test
    @DisplayName("TC2: Попытка выхода для не аутентифицированного токена.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Система не позволяет выйти пользователю, который не входил в систему.")
    public void testFailLogoutWithNotAuthorizedToken() {

        step("Пользователь пытается выйти из системы, не входя в неё предварительно - ответ ошибка 403.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(403)
                    .body("result", equalTo("ERROR"));
        });

    }

    @Test
    @DisplayName("TC3: Повторный выход для аутентифицированного токена.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Система не позволяет выйти пользователю, который не входил в систему, даже после повторной попытки.")
    public void testFailRepeatedLogoutWithNotAuthorizedToken() {

        step("Пользователь пытается выйти из системы, не входя в неё предварительно - ответ ошибка 403.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(403)
                    .body("result", equalTo("ERROR"));
        });

        step("Пользователь снова пытается выйти из системы, не входя в неё предварительно - ответ снова ошибка 403.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(403)
                    .body("result", equalTo("ERROR"));
        });

    }
}
