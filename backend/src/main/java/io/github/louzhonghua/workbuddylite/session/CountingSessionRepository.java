package io.github.louzhonghua.workbuddylite.session;

import java.util.List;
import java.util.Objects;

/** 包装另一个仓库，并统计 history() 的读取次数。 */
public final class CountingSessionRepository implements SessionRepository {
    private final SessionRepository delegate;
    private int historyCalls;

    public CountingSessionRepository(SessionRepository delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public int historyCalls() {
        return historyCalls;
    }

    @Override
    public boolean create(SessionId id) {
        return delegate.create(id);
    }

    @Override
    public void append(SessionId id, Message message) {
        delegate.append(id, message);
    }

    @Override
    public List<Message> history(SessionId id) {
        historyCalls++;
        return delegate.history(id);
    }

    @Override
    public List<Message> recentHistory(SessionId id, int limit) {
        return delegate.recentHistory(id, limit);
    }
}
