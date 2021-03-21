package ru.netology.domain;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Condition.*;

public class AuthTest {

    private final static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final static TestUser activeUser = UserGenerator.UserRequest.generate("active");
    private final static TestUser blockedUser = UserGenerator.UserRequest.generate("blocked");
    private final static TestUser notRegUser = UserGenerator.UserRequest.generate("active");

    @BeforeAll
    static void setupAll() {
        given()
                .spec(requestSpec)
                .body(activeUser)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        given()
                .spec(requestSpec)
                .body(blockedUser)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
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
    public void shouldNotRegUserGetErrorMessage() {
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
