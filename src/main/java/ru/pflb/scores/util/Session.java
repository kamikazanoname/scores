package ru.pflb.scores.util;

import java.util.Objects;

public final class Session {
    private final int userId;
    private final String sessionKey;
    private final long created;

    public Session(int userId, String sessionKey, long created) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.created = created;
    }

    public Session(int userId, String sessionKey) {
        this(userId, sessionKey, System.currentTimeMillis());
    }

    public int userId() {
        return userId;
    }

    public String sessionKey() {
        return sessionKey;
    }

    public long created() {
        return created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Session) obj;
        return this.userId == that.userId &&
                Objects.equals(this.sessionKey, that.sessionKey) &&
                this.created == that.created;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, sessionKey, created);
    }

    @Override
    public String toString() {
        return "Session[" +
                "userId=" + userId + ", " +
                "sessionKey=" + sessionKey + ", " +
                "created=" + created + ']';
    }

}
