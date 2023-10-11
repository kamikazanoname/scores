package ru.pflb.scores.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


public class SessionEvictingHolderTest {

    private final SessionEvictingHolder sessionEvictingHolder = new SessionEvictingHolder(
            1000L, 1000L, TimeUnit.MILLISECONDS);

    @Test
    public void whenSessionExpiresThenThrows() {
        var userId = 1;
        var sessionKey = sessionEvictingHolder.createSession(userId);
        var actualUserId = sessionEvictingHolder.getUserBySession(sessionKey);

        Assertions.assertEquals(userId, actualUserId);

        try {
            Thread.sleep(SessionEvictingHolder.DEFAULT_EVICTION_SCHEDULE*2);
        } catch (InterruptedException e) {
            Assertions.fail("Thread sleep was interrupted");
        }

        Assertions.assertThrows(IllegalStateException.class, () -> sessionEvictingHolder.getUserBySession(sessionKey));
    }
}
