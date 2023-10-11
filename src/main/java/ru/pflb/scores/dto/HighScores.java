package ru.pflb.scores.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class HighScores {

    private final static String DELIMITER = ",";
    private final List<Score> scores;

    public HighScores(List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return scores.stream()
                .map(Score::toString)
                .collect(Collectors.joining(DELIMITER));
    }

    public List<Score> scores() {
        return scores;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HighScores) obj;
        return Objects.equals(this.scores, that.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores);
    }

}
