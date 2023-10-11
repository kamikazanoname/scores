package ru.pflb.scores.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pflb.scores.service.ScoreService;

@RestController
public class ScoresController {

    private final ScoreService scoreService;

    @Autowired
    public ScoresController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(path = "/hiscores/{levelId}")
    public String getHighScores(@PathVariable int levelId) {
        return scoreService.highScores(levelId);
    }

    @PostMapping(path = "/score/{levelId}/{userId}")
    public void recordScore(@PathVariable int levelId, @PathVariable int userId, @RequestParam(name = "sessionKey") String sessionKey, @RequestBody String score) {
        int parsedScore = Integer.parseInt(score);
        scoreService.record(sessionKey, parsedScore, levelId);
    }
}
