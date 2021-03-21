package ru.netology.domain;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserGenerator {

    private UserGenerator() {}

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
