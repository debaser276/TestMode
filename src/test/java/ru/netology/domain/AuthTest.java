package ru.netology.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Condition.*;

public class AuthTest {

    private final static TestUser activeUser = UserGenerator.UserRequest.generate("active");
    private final static TestUser blockedUser = UserGenerator.UserRequest.generate("blocked");
    private final static TestUser notRegUser = UserGenerator.UserRequest.generate("active");

    @BeforeAll
    static void setupAll() {
        UserGenerator.regUser(activeUser);
        UserGenerator.regUser(blockedUser);
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldActiveUserLogin() {
        $("[name='login']").sendKeys(activeUser.getLogin());
        $("[name='password'").sendKeys(activeUser.getPassword());
        $(withText("Продолжить")).click();

        $(withText("Личный кабинет")).shouldHave(text("Личный кабинет"));
    }

    @Test
    public void shouldBlockedUserGetErrorMessage() {
        $("[name='login']").sendKeys(blockedUser.getLogin());
        $("[name='password'").sendKeys(blockedUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    public void shouldUserWithWrongUsernameAndPasswordGetErrorMessage() {
        $("[name='login']").sendKeys(notRegUser.getLogin());
        $("[name='password'").sendKeys(notRegUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldUserWithWrongPasswordGetErrorMessage() {
        $("[name='login']").sendKeys(activeUser.getLogin());
        $("[name='password'").sendKeys(notRegUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }
}
