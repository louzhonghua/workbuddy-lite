package io.github.louzhonghua.workbuddylite.session;

import java.util.Objects;

public record Message(Role role, String content) {
    public Message {
        Objects.requireNonNull(role, "role");
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("message content must not be blank");
        }
    }
}
