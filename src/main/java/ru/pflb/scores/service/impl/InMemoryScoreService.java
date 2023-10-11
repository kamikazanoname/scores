package ru.pflb.scores.service.impl;

import org.springframework.stereotype.Service;
import ru.pflb.scores.dto.HighScores;
import ru.pflb.scores.dto.LevelHighScores;
import ru.pflb.scores.service.LoginService;
import ru.pflb.scores.service.ScoreService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryScoreService implements ScoreService {

    private final LoginService loginService;
    private final Map<Integer, LevelHighScores> scoreboard = new ConcurrentHashMap<>();

    public InMemoryScoreService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void record(String sessionKey, int score, int levelId) {
        var userId = loginService.getUserId(sessionKey);
        recordScore(userId, levelId, score);
    }

    private void recordScore(int userId, int levelId, int score) {
        scoreboard.compute(levelId, (levelIdKey, levelHighScores) -> {
            levelHighScores = levelHighScores == null ? new LevelHighScores() : levelHighScores;
            levelHighScores.recordScore(userId, score);
            return levelHighScores;
        });
    }

    @Override
    public String highScores(int levelId) {
        return Optional.ofNullable(scoreboard.get(levelId))
                .map(LevelHighScores::getHighScores)
                .map(HighScores::toString).orElse("");
    }
}
