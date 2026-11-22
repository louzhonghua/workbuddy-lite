package io.github.louzhonghua.workbuddylite.demo;

import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.Role;
import io.github.louzhonghua.workbuddylite.session.SessionManager;

import java.util.List;

public class SessionManagerDemo {
    public static void main(String[] args) {
        var manager = new SessionManager();
        manager.createSession("weekly-report");
        manager.createSession("other-task");

        manager.addMessage("weekly-report", new Message(Role.USER, "Draft a weekly report"));
        List<Message> firstRead = manager.history("weekly-report");
        manager.addMessage("weekly-report", new Message(Role.ASSISTANT, "The draft is ready"));

        List<Message> currentHistory = manager.history("weekly-report");
        System.out.printf("Earlier snapshot: %d message(s)%n", firstRead.size());
        System.out.printf("Latest history: %d message(s)%n", currentHistory.size());
        for (Message message : currentHistory) {
            System.out.printf("%s | %s%n", message.role(), message.content());
        }
        System.out.printf("Other session: %d message(s)%n", manager.history("other-task").size());
    }
}
