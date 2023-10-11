package ru.pflb.scores.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pflb.scores.dto.LevelHighScores;
import ru.pflb.scores.service.LoginService;

import java.lang.reflect.Field;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class InMemoryScoreServiceTest {

    @Mock
    private LoginService loginService;

    private InMemoryScoreService scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new InMemoryScoreService(loginService);
    }

    @Test
    void record() {
        var sessionKey = "valid_session";
        var userId = 1;
        var score = 21;
        var levelId = 9999;

        recordScore(sessionKey, userId, score, levelId);

        var scoreboard = accessScoreboard();
        var levelHighScores = scoreboard.get(levelId).getHighScores().scores();

        Assertions.assertEquals(1, levelHighScores.size());
        var actualScore = levelHighScores.get(0);
        Assertions.assertEquals(userId, actualScore.userId());
        Assertions.assertEquals(score, actualScore.score());
    }

    private void recordScore(String sessionKey, int userId, int score, int levelId) {
        Mockito.when(loginService.getUserId(sessionKey))
                .thenReturn(userId);

        scoreService.record(sessionKey, score, levelId);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, LevelHighScores> accessScoreboard() {
        try {
            Field scoreboardField = scoreService.getClass().getDeclaredField("scoreboard");
            scoreboardField.setAccessible(true);
            return (Map<Integer, LevelHighScores>) scoreboardField.get(scoreService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    void highScores() {
        var sessionKey1 = "session1";
        var sessionKey2 = "session2";
        var sessionKey3 = "session3";
        var userId1 = 1;
        var userId2 = 2;
        var userId3 = 3;
        var score11 = 10;
        var score12 = 15;
        var score21 = 10;
        var score31 = 13;
        var levelId1 = 9999;
        var levelId2 = 3333;
        recordScore(sessionKey1, userId1, score11, levelId1);
        recordScore(sessionKey1, userId1, score12, levelId1);
        recordScore(sessionKey2, userId2, score21, levelId1);
        recordScore(sessionKey3, userId3, score31, levelId1);
        recordScore(sessionKey1, userId1, score31, levelId2);

        var expectedLevel1HighScores = "1=15,3=13,2=10";
        Assertions.assertEquals(expectedLevel1HighScores, scoreService.highScores(levelId1));
        var expectedLevel2HighScores = "1=13";
        Assertions.assertEquals(expectedLevel2HighScores, scoreService.highScores(levelId2));
    }
}