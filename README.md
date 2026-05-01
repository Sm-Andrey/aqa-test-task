# aqa-test-task
AQA Test assignment for Nord Codes

## Структура проекта
- **aqa-test-task/** — корень проекта
    - **src/test/java/com/aqa/** — исходный код тестов
    - **src/test/resources/** — конфигурации и ресурсы

## Соглашения об именовании
- Название проекта в kebab-case согласно Java conventions
- Тестовые классы с суффиксом `*Tests.java`
- Методы тестов начинаются с `test*

## Логирование
В проекте используется SLF4J с реализацией slf4j-simple:
- Настройки логирования: `src/test/resources/simplelogger.properties`
- WireMock и RestAssured замолчены до уровня WARN
- Собственные логи тестов выводятся на уровне INFO