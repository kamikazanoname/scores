package ru.pflb.scores.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class SessionEvictingHolder {
    public static final long DEFAULT_SESSION_TIMEOUT = 600000L;
    public static final long DEFAULT_EVICTION_SCHEDULE = 10000L;

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final Queue<Session> sessionsQueue = new ConcurrentLinkedQueue<>();
    private final SessionKeyGenerator sessionKeyGenerator = new SessionKeyGenerator();

    private final long sessionTimeout;

    public SessionEvictingHolder() {
        this(DEFAULT_SESSION_TIMEOUT, DEFAULT_EVICTION_SCHEDULE, TimeUnit.MILLISECONDS);
    }

    public SessionEvictingHolder(long sessionTimeout, long evictionSchedule, TimeUnit timeUnit) {
        this.sessionTimeout = sessionTimeout;

        var evictionScheduler = Executors.newScheduledThreadPool(1);
        evictionScheduler.scheduleAtFixedRate(this::evictOldSessions, this.sessionTimeout, evictionSchedule, timeUnit);
    }

    public String createSession(int userId) {
        var sessionKey = sessionKeyGenerator.generateKey();

        var session = new Session(userId, sessionKey);
        sessions.put(sessionKey, session); //note that I don't keep track of each user's sessions since not required
        sessionsQueue.add(session);
        return sessionKey;
    }

    public int getUserBySession(String sessionKey) {
        var session = sessions.get(sessionKey);
        if (sessionValid(session)) {
            return session.userId();
        }
        throw new IllegalStateException();
    }

    private boolean sessionValid(Session session) {
        return session != null && session.created() > getSessionCutoffTime();
    }

    private long getSessionCutoffTime() {
        return System.currentTimeMillis() - this.sessionTimeout;
    }

    private void evictOldSessions() {
        var cutoff = getSessionCutoffTime();
        var sessionIterator = sessionsQueue.iterator();
        Session session;
        while (sessionIterator.hasNext()) {
            session = sessionIterator.next();
            if (session.created() > cutoff) {
                break;
            }
            sessions.remove(session.sessionKey());
            sessionIterator.remove();
        }
    }
}
