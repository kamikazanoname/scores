package ru.pflb.scores.service;

public interface LoginService {

    String login(int userId, String password);

    int getUserId(String sessionKey);
}
