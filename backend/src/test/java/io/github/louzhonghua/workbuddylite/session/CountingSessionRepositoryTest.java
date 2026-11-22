package io.github.louzhonghua.workbuddylite.session;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountingSessionRepositoryTest {
    private final Message request = new Message(Role.USER, "Draft a weekly report");
    private final Message reply = new Message(Role.ASSISTANT, "The draft is ready");
    private final CountingSessionRepository repository =
            new CountingSessionRepository(new InMemorySessionRepository());

    @Test
    void countsHistoryReadsAndReturnsTheDelegatedContent() {
        var manager = new SessionManager(repository);
        manager.createSession("report");
        manager.addMessage("report", request);

        List<Message> firstRead = manager.history("report");
        manager.addMessage("report", reply);
        List<Message> secondRead = manager.history("report");

        assertEquals(2, repository.historyCalls());
        assertEquals(List.of(request), firstRead);
        assertEquals(List.of(request, reply), secondRead);
    }

    @Test
    void doesNotCountWritesOrRecentHistory() {
        var manager = new SessionManager(repository);
        manager.createSession("report");
        manager.addMessage("report", request);
        manager.recentHistory("report", 1);

        assertEquals(0, repository.historyCalls());
        assertEquals(List.of(request), manager.recentHistory("report", 1));
        assertEquals(0, repository.historyCalls());
    }

    @Test
    void delegatesSessionLifecycleAndErrors() {
        var manager = new SessionManager(repository);
        assertTrue(repository.create(new SessionId("report")));
        manager.addMessage("report", request);

        assertThrows(NoSuchElementException.class, () -> manager.history("missing"));
        assertEquals(1, repository.historyCalls());
    }
}
