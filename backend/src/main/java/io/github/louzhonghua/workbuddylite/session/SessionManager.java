package io.github.louzhonghua.workbuddylite.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/** In-memory session manager for single-threaded use. */
public final class SessionManager {
    private final Map<String, Session> sessions = new HashMap<>();

    public void createSession(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("session ID must not be blank");
        }
        if (sessions.putIfAbsent(sessionId, new Session()) != null) {
            throw new IllegalArgumentException("session already exists: " + sessionId);
        }
    }

    public void addMessage(String sessionId, Message message) {
        requireSession(sessionId).append(message);
    }

    public List<Message> history(String sessionId) {
        return requireSession(sessionId).history();
    }

    public List<Message> recentHistory(String sessionId, int limit) {
        return requireSession(sessionId).recentHistory(limit);
    }

    private Session requireSession(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            throw new NoSuchElementException("unknown session: " + sessionId);
        }
        return session;
    }

    private static final class Session {
        private final List<Message> messages = new ArrayList<>();

        void append(Message message) {
            messages.add(Objects.requireNonNull(message, "message"));
        }

        List<Message> history() {
            return List.copyOf(messages);
        }

        List<Message> recentHistory(int limit) {
            if (limit < 0) {
                throw new IllegalArgumentException("limit must not be negative: " + limit);
            }
            return List.copyOf(messages.subList(Math.max(0, messages.size() - limit), messages.size()));
        }
    }
}
