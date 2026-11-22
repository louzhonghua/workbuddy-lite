package io.github.louzhonghua.workbuddylite.demo;

import io.github.louzhonghua.workbuddylite.session.Message;
import io.github.louzhonghua.workbuddylite.session.Role;
import io.github.louzhonghua.workbuddylite.session.SessionId;
import io.github.louzhonghua.workbuddylite.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class SessionKeyDemo {
    public static void main(String[] args) {
        SessionId storedKey = new SessionId("weekly-report");
        SessionId lookupKey = new SessionId("weekly-report");

        System.out.println("Same reference: " + (storedKey == lookupKey));
        System.out.println("Equal values: " + storedKey.equals(lookupKey));
        System.out.println("Same hash code: " + (storedKey.hashCode() == lookupKey.hashCode()));

        var manager = new SessionManager();
        manager.createSession(storedKey.value());
        manager.addMessage(storedKey.value(), new Message(Role.USER, "Draft a weekly report"));
        System.out.println("Messages found: " + manager.history(lookupKey.value()).size());

        String first = "FB";
        String second = "Ea";
        Map<String, String> collisions = new HashMap<>();
        collisions.put(first, "Session A");
        collisions.put(second, "Session B");
        System.out.println("String hash collision: " + (first.hashCode() == second.hashCode()));
        System.out.printf("After collision: %s, %s%n", collisions.get(first), collisions.get(second));
    }
}
