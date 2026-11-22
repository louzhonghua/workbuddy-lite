package io.github.louzhonghua.workbuddylite.session;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionManagerTest {
    private final SessionManager manager = new SessionManager();
    private final Message request = new Message(Role.USER, "Draft a weekly report");
    private final Message reply = new Message(Role.ASSISTANT, "The draft is ready");

    @Test
    void appendsMessagesInOrderToTheRequestedSession() {
        manager.createSession("report");
        manager.createSession("other");

        manager.addMessage("report", request);
        manager.addMessage("report", reply);

        assertEquals(List.of(request, reply), manager.history("report"));
        assertEquals(List.of(), manager.history("other"));
    }

    @Test
    void returnsAnUnmodifiableSnapshot() {
        manager.createSession("report");
        manager.addMessage("report", request);
        List<Message> snapshot = manager.history("report");

        manager.addMessage("report", reply);

        assertEquals(List.of(request), snapshot);
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(reply));
    }

    @Test
    void rejectsDuplicateSessionWithoutErasingItsHistory() {
        manager.createSession("report");
        manager.addMessage("report", request);

        assertThrows(IllegalArgumentException.class, () -> manager.createSession("report"));
        assertEquals(List.of(request), manager.history("report"));
    }

    @Test
    void reportsAnUnknownSessionForReadsAndWrites() {
        assertThrows(NoSuchElementException.class, () -> manager.history("missing"));
        assertThrows(NoSuchElementException.class, () -> manager.addMessage("missing", request));
    }

    @Test
    void rejectsBlankSessionIdsAndMessageContent() {
        assertThrows(IllegalArgumentException.class, () -> manager.createSession(" "));
        assertThrows(IllegalArgumentException.class, () -> new Message(Role.USER, " "));
    }
}
