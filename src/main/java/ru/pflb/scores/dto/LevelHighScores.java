package ru.pflb.scores.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class LevelHighScores {

    private final static int DEFAULT_SCORES_LIMIT = 15;

    private final TreeMap<Integer, Set<Score>> scores = new TreeMap<>(); //таблица из значения рекорда в список рекордов пользователей
    private final Map<Integer, Integer> userHighScores = new ConcurrentHashMap<>(); //таблица из номера пользователя в его лучший результат

    //записать результат пользователя на уровне
    public void recordScore(int userId, int score) {
        userHighScores.compute(userId, (userIdKey, oldScore) -> {
            oldScore = oldScore == null ? 0 : oldScore;
            if (score > oldScore) {
                updateHighScores(userId, oldScore, score);
                return score;
            } else {
                return oldScore;
            }
        });

//      потоконебезопасной
//        var oldScore = userHighScores.get(userId);
//        oldScore  = oldScore == null ? 0 : oldScore;
//        if (score > oldScore) {
//            updateHighScores(userId, oldScore, score);
//            userHighScores.put(userId, score);
//        }
    }

    private void updateHighScores(int userId, int oldScore, int newScoreValue) {
        synchronized (scores) {
            //удалить старый рекорд этого пользователя
            scores.computeIfPresent(oldScore, (scoreValue, scoreListPerValue) -> {
                scoreListPerValue.removeIf(score -> score.userId() == userId);
                return scoreListPerValue;
            });
            //добавить
            scores.compute(newScoreValue, (k, v) -> {
                var score = new Score(userId, newScoreValue);
                Set<Score> set = v == null ? new HashSet<>() : v;//not synchronized, but we won't be modifying it concurrently
                set.add(score);
                return set;
            });
        }
    }

    public HighScores getHighScores() {
        return getHighScores(DEFAULT_SCORES_LIMIT);
    }

    //
    public HighScores getHighScores(int limit) {
        return new HighScores(
                scores.descendingMap().entrySet().stream().flatMap(entry -> entry.getValue().stream())
                        .limit(limit).toList());
    }
}
