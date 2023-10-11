package ru.pflb.scores.service;

public interface ScoreService {

    void record(String sessionKey, int score, int levelId) throws IllegalStateException;

    String highScores(int levelId);
}
