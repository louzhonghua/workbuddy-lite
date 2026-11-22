package io.github.louzhonghua.workbuddylite.session;

import java.util.List;
import java.util.NoSuchElementException;

/** 按插入顺序存储会话，并返回消息快照。 */
public interface SessionRepository {
    /** 创建一个空会话；ID 已存在时返回 false。 */
    boolean create(SessionId id);

    /**
     * 向已存在的会话追加一条消息。
     * @throws NoSuchElementException 会话不存在时抛出
     */
    void append(SessionId id, Message message);

    /**
     * 返回指定会话的不可变消息快照。
     * @throws NoSuchElementException 会话不存在时抛出
     */
    List<Message> history(SessionId id);

    /**
     * 返回最近 {@code limit} 条消息的不可变快照，最旧的在前。
     * @throws NoSuchElementException 会话不存在时抛出
     * @throws IllegalArgumentException 对已存在的会话传入负数 {@code limit} 时抛出
     */
    List<Message> recentHistory(SessionId id, int limit);
}
