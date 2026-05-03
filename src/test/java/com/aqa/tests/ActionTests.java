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
import static com.aqa.clients.WireMockClient.stubDoActionFail;
import static com.aqa.clients.WireMockClient.stubDoActionSuccess;
import static com.aqa.data.TestData.ACTION;
import static com.aqa.data.TestData.LOGIN;
import static com.aqa.data.TestData.LOGOUT;
import static com.aqa.data.TestData.SHORT_TOKEN;
import static com.aqa.data.TestData.VALID_TOKEN;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;

@Epic("Выполнение действия.")
@Feature("ACTION")
public class ActionTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешное выполнение действия с предварительно аутентифицированным токеном.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система подтверждает запрос на действие, если пользователь прошел аутентификацию в системе.")
    public void testSuccessDoAction() {

        step("Подготовка: Регистрация пользователя во внешней системе с валидным ключом " + VALID_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthSuccess(VALID_TOKEN);
        });

        step("Подготовка: Внешний сервис подтверждает право на выполнение операции для текущего пользователя.", () -> {
            stubDoActionSuccess(VALID_TOKEN);
        });

        step("Пользователь успешно входит в систему.", () -> {
            preparationRequest(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь выполняет действие в системе.", () -> {
            preparationRequest(VALID_TOKEN, ACTION)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь выходит из системы.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });
    }

    @Test
    @DisplayName("TC2: Попытка выполнить действие без предварительной аутентификации.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система не позволяет выполнять действия, если пользователь еще не прошел аутентификации в системе.")
    public void testFailDoActionWithoutToken() {

        step("Подготовка: Внешний сервис сообщает об ошибке при обработке операции.", () -> {
            stubDoActionFail(VALID_TOKEN);
        });

        step("Пользователь выполняет действие в системе, но без успешно.", () -> {
            preparationRequest(VALID_TOKEN, ACTION)
                    .then()
                    .statusCode(403)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("TC3: Попытка выполнить действие после выхода из системы.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Система не позволяет выполнять действия, если пользователь уже вышел из системы.")
    public void testFailDoActionAfterLogout() {

        step("Подготовка: Регистрация пользователя во внешней системе с валидным ключом " + VALID_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthSuccess(VALID_TOKEN);
        });

        step("Подготовка: Внешний сервис сообщает об ошибке при обработке операции.", () -> {
            stubDoActionFail(VALID_TOKEN);
        });

        step("Пользователь успешно входит в систему.", () -> {
            preparationRequest(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь выходит из системы.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь пытает выполнить действие в системе, но без успешно.", () -> {
            preparationRequest(VALID_TOKEN, ACTION)
                    .then()
                    .statusCode(403)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("TC4: Действие с невалидным токеном (короткий/спецсимволы).")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Система не позволяет выполнять действия с токеном, который не проходил аутентификацию.")
    public void testFailDoActionWithInvalidToken() {

        step("Подготовка: Регистрация пользователя во внешней системе с валидным ключом " + VALID_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthSuccess(VALID_TOKEN);
        });

        step("Подготовка: Внешний сервис сообщает об ошибке при обработке операции.", () -> {
            stubDoActionFail(SHORT_TOKEN);
        });

        step("Пользователь успешно входит в систему.", () -> {
            preparationRequest(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });

        step("Пользователь пытает выполнить действие в системе с токеном длиной " + SHORT_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(SHORT_TOKEN, ACTION)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });

        step("Пользователь выходит из системы.", () -> {
            preparationRequest(VALID_TOKEN, LOGOUT)
                    .then()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
        });
    }

    @Test
    @DisplayName("TC5: Ошибка при попытке выполнить действие с невалидным токеном")
    @Severity(SeverityLevel.NORMAL)
    @Description("Система не позволяет выполнять действия с токеном, если токен не соответствует формату (32 символа A-Z0-9)")
    public void testActionWithInvalidTokenFormat() {

        step("Подготовка: Внешний сервис сообщает об ошибке при обработке операции.", () -> {
            stubDoActionFail(SHORT_TOKEN);
        });

        step("Пользователь пытается выполнить действие с токеном длиной " + SHORT_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(SHORT_TOKEN, ACTION)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });
    }
}
