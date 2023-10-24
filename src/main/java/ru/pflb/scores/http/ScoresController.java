package ru.pflb.scores.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.pflb.scores.service.ScoreService;

@RestController
public class ScoresController {

    private final ScoreService scoreService;

    @Autowired
    public ScoresController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(path = "/highscores/{levelId}")
    public String getHighScores(@PathVariable int levelId) {
        return scoreService.highScores(levelId);
    }

    @PostMapping(path = "/score/{levelId}/{userId}")
    public void recordScore(@PathVariable int levelId, @PathVariable int userId, @RequestBody String score, @CookieValue("SessionKey") String sessionKey) {
        int parsedScore = Integer.parseInt(score);
        scoreService.record(sessionKey, parsedScore, levelId);
    }
}
