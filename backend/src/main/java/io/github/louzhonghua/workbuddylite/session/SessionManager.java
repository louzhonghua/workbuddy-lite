package io.github.louzhonghua.workbuddylite.session;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class SessionManager {
    private final SessionRepository repository;

    public SessionManager() {
        this(new InMemorySessionRepository());
    }

    public SessionManager(SessionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public void createSession(String sessionId) {
        SessionId key = new SessionId(sessionId);
        if (!repository.create(key)) {
            throw new IllegalArgumentException("session already exists: " + sessionId);
        }
    }

    public void addMessage(String sessionId, Message message) {
        repository.append(requireSessionId(sessionId), message);
    }

    public List<Message> history(String sessionId) {
        return repository.history(requireSessionId(sessionId));
    }

    public List<Message> recentHistory(String sessionId, int limit) {
        return repository.recentHistory(requireSessionId(sessionId), limit);
    }

    private SessionId requireSessionId(String sessionId) {
        if (!SessionId.isValid(sessionId)) {
            throw new NoSuchElementException("unknown session: " + sessionId);
        }
        return new SessionId(sessionId);
    }
}
