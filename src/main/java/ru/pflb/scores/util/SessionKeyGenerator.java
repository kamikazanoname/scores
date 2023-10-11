package ru.pflb.scores.util;

import java.util.concurrent.ThreadLocalRandom;

public class SessionKeyGenerator {
    //taken from stackoverflow as a placeholder except changed Random to ThreadLocalRandom
    public String generateKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;

        return ThreadLocalRandom.current().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }
}
