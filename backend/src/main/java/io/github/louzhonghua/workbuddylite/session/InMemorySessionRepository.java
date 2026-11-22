package io.github.louzhonghua.workbuddylite.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/** In-memory storage for single-threaded use. */
public final class InMemorySessionRepository implements SessionRepository {
    private final Map<SessionId, List<Message>> sessions = new HashMap<>();

    @Override
    public boolean create(SessionId id) {
        return sessions.putIfAbsent(Objects.requireNonNull(id, "id"), new ArrayList<>()) == null;
    }

    @Override
    public void append(SessionId id, Message message) {
        requireMessages(id).add(Objects.requireNonNull(message, "message"));
    }

    @Override
    public List<Message> history(SessionId id) {
        return List.copyOf(requireMessages(id));
    }

    @Override
    public List<Message> recentHistory(SessionId id, int limit) {
        List<Message> messages = requireMessages(id);
        if (limit < 0) {
            throw new IllegalArgumentException("limit must not be negative: " + limit);
        }
        int start = Math.max(0, messages.size() - limit);
        return List.copyOf(messages.subList(start, messages.size()));
    }

    private List<Message> requireMessages(SessionId id) {
        List<Message> messages = sessions.get(Objects.requireNonNull(id, "id"));
        if (messages == null) {
            throw new NoSuchElementException("unknown session: " + id.value());
        }
        return messages;
    }
}
