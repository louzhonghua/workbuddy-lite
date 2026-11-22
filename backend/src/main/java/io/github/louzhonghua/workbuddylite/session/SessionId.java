package io.github.louzhonghua.workbuddylite.session;

public record SessionId(String value) {
    public SessionId {
        if (!isValid(value)) {
            throw new IllegalArgumentException("session ID must not be blank");
        }
    }

    static boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}
