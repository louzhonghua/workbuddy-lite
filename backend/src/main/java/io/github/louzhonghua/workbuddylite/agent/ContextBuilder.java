package io.github.louzhonghua.workbuddylite.agent;

import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.SessionManager;

import java.util.List;
import java.util.Objects;

public final class ContextBuilder {
    private final SessionManager sessions;

    public ContextBuilder(SessionManager sessions) {
        this.sessions = Objects.requireNonNull(sessions, "sessions");
    }

    /** 取最近的会话消息，按原顺序转换为不可修改的模型输入列表。 */
    public List<ModelMessage> build(String sessionId, int maxMessages) {
        return sessions.recentHistory(sessionId, maxMessages).stream()
                .map(ContextBuilder::toModelMessage)
                .toList();
    }

    private static ModelMessage toModelMessage(Message message) {
        String role = switch (message.role()) {
            case USER -> "user";
            case ASSISTANT -> "assistant";
        };
        return new ModelMessage(role, message.content());
    }
}
