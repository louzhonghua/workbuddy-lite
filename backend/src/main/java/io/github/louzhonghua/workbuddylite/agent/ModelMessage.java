package io.github.louzhonghua.workbuddylite.agent;

/** 用于构建模型输入的消息。 */
public record ModelMessage(String role, String content) {}
