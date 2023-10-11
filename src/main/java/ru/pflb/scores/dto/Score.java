package ru.pflb.scores.dto;

import java.util.Objects;

public final class Score {

    private static final String EQUALS = "=";
    private final int userId;
    private final int score;

    public Score(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    @Override
    public String toString() {
        return userId + EQUALS + score;
    }

    public int userId() {
        return userId;
    }

    public int score() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Score) obj;
        return this.userId == that.userId &&
                this.score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, score);
    }

}
