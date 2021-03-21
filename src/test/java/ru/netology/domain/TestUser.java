package ru.netology.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TestUser {
    private final String login;
    private final String password;
    private final String status;
}
