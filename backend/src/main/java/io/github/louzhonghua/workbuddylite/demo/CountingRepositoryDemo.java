package io.github.louzhonghua.workbuddylite.demo;

import io.github.louzhonghua.workbuddylite.session.CountingSessionRepository;
import io.github.louzhonghua.workbuddylite.session.InMemorySessionRepository;
import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.Role;
import io.github.louzhonghua.workbuddylite.session.SessionManager;

import java.util.List;

public class CountingRepositoryDemo {
    public static void main(String[] args) {
        var repository = new CountingSessionRepository(new InMemorySessionRepository());
        var manager = new SessionManager(repository);

        manager.createSession("weekly-report");
        manager.addMessage("weekly-report", new Message(Role.USER, "Draft a weekly report"));

        List<Message> firstRead = manager.history("weekly-report");
        manager.addMessage("weekly-report", new Message(Role.ASSISTANT, "The draft is ready"));
        List<Message> secondRead = manager.history("weekly-report");

        System.out.printf("History calls: %d%n", repository.historyCalls());
        System.out.printf("First read: %d message(s)%n", firstRead.size());
        for (Message message : firstRead) {
            System.out.printf("%s | %s%n", message.role(), message.content());
        }
        System.out.printf("Second read: %d message(s)%n", secondRead.size());
        for (Message message : secondRead) {
            System.out.printf("%s | %s%n", message.role(), message.content());
        }
    }
}
