package ru.pflb.scores.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;


public class SessionKeyGeneratorTest {

    private final SessionKeyGenerator sessionKeyGenerator = new SessionKeyGenerator();

    @Test
    public void whenCalledGeneratesReasonablyRandomNumbers() {
        var sessionCount = 100;
        var sessions = new HashSet<String>();

        for (int i = 0; i < sessionCount; i++) {
            sessions.add(sessionKeyGenerator.generateKey());
        }
        Assertions.assertEquals(sessionCount, sessions.size());
    }
}
