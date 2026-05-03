package com.aqa.tests;

import io.qameta.allure.Description;
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
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


@Epic("Вход в систему.")
@Feature("LOGIN")
public class LoginTests extends BaseTest {

    @Test
    @DisplayName("TC1: Успешная аутентификация с валидным токеном.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Пользователь может войти в систему, используя корректный 32-символьный токен.")
    public void testSuccessLogin() {

        step("Подготовка: Регистрация пользователя во внешней системе с валидным токеном " + VALID_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthSuccess(VALID_TOKEN);
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
    }

    @Test
    @DisplayName("ТС2: Аутентификация с коротким логином.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Пользователь не может войти в систему, используя короткий 31-символьный токен.")
    public void testLoginWithSnortToken() {

        step("Подготовка: внешний сервис отклоняет короткий токен " + SHORT_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthFail(SHORT_TOKEN);
        });

        step("Пользователь пытается войти с токеном длиной " + SHORT_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(SHORT_TOKEN, LOGIN)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("ТС3: Аутентификация с длинным токеном.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Пользователь не может войти в систему, используя длинный 33-символьный токен.")
    public void testLoginWithLongToken() {

        step("Подготовка: внешний сервис отклоняет длинный токен " + LONG_TOKEN.substring(0, 8) + "***.", () -> {
            stubAuthFail(LONG_TOKEN);
        });

        step("Пользователь пытается войти с токеном длиной " + LONG_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(LONG_TOKEN, LOGIN)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("ТС4: Аутентификация с токеном со спецсимволами.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Пользователь не может войти в систему, используя токеном со спецсимволами.")
    public void testLoginWithSpecialToken() {

        step("Подготовка: внешний сервис отклоняет токен со спецсимволами " + TOKEN_WITH_SPECIAL.substring(0, 8) + "***.", () -> {
            stubAuthFail(TOKEN_WITH_SPECIAL);
        });

        step("Пользователь пытается войти с токеном со спецсимволами " + TOKEN_WITH_SPECIAL.substring(0, 8) + "***, но без успешно.", () -> {
            preparationRequest(TOKEN_WITH_SPECIAL, LOGIN)
                    .then()
                    .statusCode(400)
                    .body("result", equalTo("ERROR"))
                    .body("message", containsString("token: должно соответствовать"));
        });
    }


    @Test
    @DisplayName("TC5: Аутентификация с пустым токеном")
    @Severity(SeverityLevel.NORMAL)
    @Description("Пользователь не может войти в систему, используя пустой токен без символов.")
    public void testLoginWithEmptyToken() {

        step("Подготовка: внешний сервис отклоняет пустой токен.", () -> {
            stubAuthFail(EMPTY_TOKEN);
        });

        step("Пользователь пытается войти с токеном длиной " + EMPTY_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(EMPTY_TOKEN, LOGIN)
                    .then()
                    .statusCode(400)
                    .body("message", containsString("token: должно соответствовать"));
        });
    }


    @Test
    @DisplayName("TC6: Аутентификация, когда внешний сервис возвращает ошибку.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Пользователь не может войти в систему, когда внешний сервис не доступен и возвращает ошибку.")
    public void testLoginWithExternalServiceError() {

        step("Подготовка: внешний сервис не доступен и возвращает 500 ошибку.", () -> {
            stubAuthServerError(VALID_TOKEN);
        });

        step("Пользователь пытается войти с валидным ключом длиной " + VALID_TOKEN.length() + " символов, но без успешно.", () -> {
            preparationRequest(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(500)
                    .body("result", equalTo("ERROR"));
        });
    }

    @Test
    @DisplayName("TC7: Аутентификация без API-ключа.")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Пользователь может войти в систему, используя корректный 32-символьный токен, но без API-ключа.")
    public void testLoginWithoutApiKey() {

        step("Пользователь пытается войти с валидным ключом длиной " + VALID_TOKEN.length() + " символов, но без API-ключа и без успешно.", () -> {
            preparationRequestWithoutApiKey(VALID_TOKEN, LOGIN)
                    .then()
                    .statusCode(401)
                    .body("result", equalTo("ERROR"));
        });
    }
}
