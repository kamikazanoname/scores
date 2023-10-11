package ru.pflb.scores.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pflb.scores.util.SessionEvictingHolder;

@ExtendWith(MockitoExtension.class)
class NoAuthLoginServiceTest {

    @Mock
    private SessionEvictingHolder sessionEvictingHolder;

    private NoAuthLoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new NoAuthLoginService(sessionEvictingHolder);
    }

    @Test
    void loginReturnsSessionKey() {
        var userId = 1;
        var sessionKey = "valid_session";

        Mockito.when(sessionEvictingHolder.createSession(userId))
                .thenReturn(sessionKey);

        var actualSessionKey = loginService.login(userId);

        Assertions.assertEquals(sessionKey, actualSessionKey);
    }

    @Test
    void whenCheckValidSessionThenReturnsUserId() {
        var userId = 1;
        var sessionKey = "valid_session";

        Mockito.when(sessionEvictingHolder.getUserBySession(sessionKey))
                .thenReturn(userId);

        var actualUserId = loginService.getUserId(sessionKey);

        Assertions.assertEquals(userId, actualUserId);
    }

    @Test
    void whenCheckExpiredSessionThenThrows() {
        var sessionKey = "expired_session";

        Mockito.when(sessionEvictingHolder.getUserBySession(sessionKey))
                .thenThrow(new IllegalStateException());

        Assertions.assertThrows(IllegalStateException.class, () ->
                loginService.getUserId(sessionKey));
    }
}