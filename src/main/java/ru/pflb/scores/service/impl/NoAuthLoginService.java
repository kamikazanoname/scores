package ru.pflb.scores.service.impl;

import org.springframework.stereotype.Service;
import ru.pflb.scores.service.LoginService;
import ru.pflb.scores.util.SessionEvictingHolder;

@Service
public class NoAuthLoginService implements LoginService {

    public NoAuthLoginService(SessionEvictingHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    private final SessionEvictingHolder sessionHolder;

    //для тестов
    @Override
    public int getUserId(String sessionKey) {
        return sessionHolder.getUserBySession(sessionKey);
    }

    @Override
    public String login(int userId, String password) {
        return sessionHolder.createSession(userId);
    }
}
