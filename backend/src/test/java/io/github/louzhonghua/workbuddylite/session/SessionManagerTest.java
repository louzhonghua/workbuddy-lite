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
    private final Message followUp = new Message(Role.USER, "Send it to the team");
    private final Message laterMessage = new Message(Role.ASSISTANT, "Sent");

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
        assertThrows(NoSuchElementException.class, () -> manager.recentHistory("missing", 1));
    }

    @Test
    void resolvesEquivalentIdsAndKeepsDistinctSessionsSeparate() {
        manager.createSession("FB");
        manager.createSession("Ea");
        manager.addMessage(new String("FB"), request);
        manager.addMessage(new String("Ea"), reply);

        assertEquals(List.of(request), manager.history(new String("FB")));
        assertEquals(List.of(reply), manager.history(new String("Ea")));
    }

    @Test
    void stillReportsAnUnknownSessionForInvalidLookupIds() {
        assertThrows(NoSuchElementException.class, () -> manager.history(null));
        assertThrows(NoSuchElementException.class, () -> manager.history(" "));
    }

    @Test
    void returnsTheMostRecentMessagesInTheirOriginalOrder() {
        manager.createSession("report");
        manager.addMessage("report", request);
        manager.addMessage("report", reply);
        manager.addMessage("report", followUp);

        assertEquals(List.of(reply, followUp), manager.recentHistory("report", 2));
    }

    @Test
    void returnsAllMessagesWhenLimitExceedsTheHistorySize() {
        manager.createSession("report");
        manager.addMessage("report", request);
        manager.addMessage("report", reply);
        manager.addMessage("report", followUp);

        assertEquals(List.of(request, reply, followUp), manager.recentHistory("report", 10));
    }

    @Test
    void returnsAnEmptyListForZeroLimit() {
        manager.createSession("report");
        manager.addMessage("report", request);

        assertEquals(List.of(), manager.recentHistory("report", 0));
    }

    @Test
    void rejectsNegativeLimit() {
        manager.createSession("report");
        manager.addMessage("report", request);

        assertThrows(IllegalArgumentException.class, () -> manager.recentHistory("report", -1));
    }

    @Test
    void recentHistorySnapshotKeepsItsContentAfterNewMessages() {
        manager.createSession("report");
        manager.addMessage("report", request);
        manager.addMessage("report", reply);
        manager.addMessage("report", followUp);
        List<Message> recent = manager.recentHistory("report", 2);

        manager.addMessage("report", laterMessage);

        assertEquals(List.of(reply, followUp), recent);
        assertEquals(List.of(followUp, laterMessage), manager.recentHistory("report", 2));
    }

    @Test
    void rejectsBlankSessionIdsAndMessageContent() {
        assertThrows(IllegalArgumentException.class, () -> manager.createSession(" "));
        assertThrows(IllegalArgumentException.class, () -> new Message(Role.USER, " "));
    }
}
