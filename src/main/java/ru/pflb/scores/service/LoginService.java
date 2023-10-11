package ru.pflb.scores.service;

public interface LoginService {

    String login(int userId);

    int getUserId(String sessionKey);
}
