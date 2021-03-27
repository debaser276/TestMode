package ru.netology.domain;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

import java.util.Locale;

public class UserGenerator {

    private UserGenerator() {}

    private final static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void regUser(TestUser user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static class UserRequest {

        private UserRequest() {}

        public static TestUser generate(String status) {
            Faker faker = new Faker(new Locale("ru"));
            return new TestUser(
                    faker.name().username(),
                    faker.lorem().characters(8, 16),
                    status
            );
        }
    }

}
