package io.github.louzhonghua.workbuddylite.agent;

import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.Role;
import io.github.louzhonghua.workbuddylite.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContextBuilderTest {
    private static final String SESSION_ID = "weekly-report";

    private final SessionManager sessions = new SessionManager();
    private final ContextBuilder builder = new ContextBuilder(sessions);

    @BeforeEach
    void createSessionWithHistory() {
        sessions.createSession(SESSION_ID);
        sessions.addMessage(SESSION_ID, new Message(Role.USER, "Write a weekly report"));
        sessions.addMessage(SESSION_ID, new Message(Role.ASSISTANT, "The draft is ready"));
        sessions.addMessage(SESSION_ID, new Message(Role.USER, "List the risks separately"));
    }

    @Test
    void mapsLatestMessagesToModelRolesInHistoryOrder() {
        assertEquals(List.of(
                new ModelMessage("assistant", "The draft is ready"),
                new ModelMessage("user", "List the risks separately")
        ), builder.build(SESSION_ID, 2));
        assertEquals(3, sessions.history(SESSION_ID).size());
    }

    @Test
    void returnsNoMessagesForZeroLimit() {
        assertEquals(List.of(), builder.build(SESSION_ID, 0));
    }

    @Test
    void returnsAnUnmodifiableSnapshot() {
        List<ModelMessage> context = builder.build(SESSION_ID, 1);
        sessions.addMessage(SESSION_ID, new Message(Role.ASSISTANT, "Two risks identified"));

        assertEquals(List.of(new ModelMessage("user", "List the risks separately")), context);
        assertThrows(UnsupportedOperationException.class, context::clear);
    }

    @Test
    void preservesSessionLookupAndLimitErrors() {
        assertThrows(NoSuchElementException.class, () -> builder.build("missing", 1));
        assertThrows(IllegalArgumentException.class, () -> builder.build(SESSION_ID, -1));
    }
}
