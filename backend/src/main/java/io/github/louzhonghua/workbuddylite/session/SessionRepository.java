package io.github.louzhonghua.workbuddylite.session;

import java.util.List;
import java.util.NoSuchElementException;

/** Stores sessions and returns message snapshots in insertion order. */
public interface SessionRepository {
    /** Creates an empty session and returns false if the ID already exists. */
    boolean create(SessionId id);

    /**
     * Appends a message to an existing session.
     * @throws NoSuchElementException if the session does not exist
     */
    void append(SessionId id, Message message);

    /**
     * Returns an unmodifiable snapshot for an existing session.
     * @throws NoSuchElementException if the session does not exist
     */
    List<Message> history(SessionId id);

    /**
     * Returns an unmodifiable snapshot of the last {@code limit} messages, oldest first.
     * @throws NoSuchElementException if the session does not exist
     * @throws IllegalArgumentException if an existing session is queried with a negative limit
     */
    List<Message> recentHistory(SessionId id, int limit);
}
