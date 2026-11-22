package io.github.louzhonghua.workbuddylite.demo;

import io.github.louzhonghua.workbuddylite.agent.ContextBuilder;
import io.github.louzhonghua.workbuddylite.agent.ModelMessage;
import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.Role;
import io.github.louzhonghua.workbuddylite.session.SessionManager;

import java.util.List;

public class ContextPreviewDemo {
    public static void main(String[] args) {
        var sessions = new SessionManager();
        String sessionId = "weekly-report";
        sessions.createSession(sessionId);
        sessions.addMessage(sessionId, new Message(Role.USER, "请根据会议纪要写一份周报。"));
        sessions.addMessage(sessionId, new Message(Role.ASSISTANT, "周报草稿已完成。"));
        sessions.addMessage(sessionId, new Message(Role.USER, "请把风险单独列出来。"));

        var contextBuilder = new ContextBuilder(sessions);
        List<ModelMessage> context = contextBuilder.build(sessionId, 2);

        System.out.println("模型输入消息数 " + context.size());
        for (ModelMessage message : context) {
            System.out.printf("%s | %s%n", message.role(), message.content());
        }
        System.out.println("会话历史消息数 " + sessions.history(sessionId).size());
    }
}
