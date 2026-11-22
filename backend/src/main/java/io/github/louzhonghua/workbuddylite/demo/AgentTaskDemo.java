package io.github.louzhonghua.workbuddylite.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AgentTaskDemo {
    record TaskRequest(String runId, String prompt, long delayMillis) {}

    enum RunStatus {
        QUEUED, RUNNING, SUCCEEDED, FAILED
    }

    record RunSnapshot(String runId, RunStatus status, String detail) {}

    private static final long START = System.nanoTime();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String mode = args.length > 0 ? args[0] : "batch";
        var requests = List.of(
                new TaskRequest("run-1", "Summarize meeting notes", 800),
                new TaskRequest("run-2", "Draft a weekly report", 100),
                new TaskRequest("run-3", "   ", 0)
        );

        System.out.printf("=== mode: %s ===%n", mode);
        long startNanos = System.nanoTime();
        if ("seq".equals(mode)) {
            runSequential(requests);
        } else {
            runBatch(requests);
        }
        long totalMs = (System.nanoTime() - startNanos) / 1_000_000;
        System.out.printf("=== total wall time: %d ms ===%n", totalMs);
    }

    private static void runBatch(List<TaskRequest> requests) throws InterruptedException, ExecutionException {
        var results = new ArrayList<Future<RunSnapshot>>();

        try (ExecutorService workers = Executors.newFixedThreadPool(2)) {
            for (TaskRequest request : requests) {
                log(new RunSnapshot(request.runId(), RunStatus.QUEUED, "accepted, delay=" + request.delayMillis() + "ms"));
                results.add(workers.submit(() -> run(request)));
            }

            System.out.println("--- Results in submission order ---");
            for (Future<RunSnapshot> result : results) {
                printResult(result.get());
            }
        }
    }

    private static void runSequential(List<TaskRequest> requests) throws InterruptedException, ExecutionException {
        try (ExecutorService workers = Executors.newFixedThreadPool(2)) {
            for (TaskRequest request : requests) {
                log(new RunSnapshot(request.runId(), RunStatus.QUEUED, "accepted, delay=" + request.delayMillis() + "ms"));
                Future<RunSnapshot> future = workers.submit(() -> run(request));
                System.out.printf("t=%4dms [main] waiting for %s ...%n", elapsedMs(), request.runId());
                printResult(future.get());
            }
        }
    }

    private static RunSnapshot run(TaskRequest request) {
        log(new RunSnapshot(request.runId(), RunStatus.RUNNING, "worker started"));

        try {
            Thread.sleep(request.delayMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            RunSnapshot aborted = new RunSnapshot(request.runId(), RunStatus.FAILED, "interrupted");
            log(aborted);
            return aborted;
        }

        RunSnapshot result = request.prompt().isBlank()
                ? new RunSnapshot(request.runId(), RunStatus.FAILED, "prompt must not be blank")
                : new RunSnapshot(request.runId(), RunStatus.SUCCEEDED,
                        "Mock result for: " + request.prompt());

        log(result);
        return result;
    }

    private static void printResult(RunSnapshot snapshot) {
        System.out.printf("t=%4dms [main] result: %s -> %s | %s%n",
                elapsedMs(), snapshot.runId(), snapshot.status(), snapshot.detail());
    }

    private static void log(RunSnapshot snapshot) {
        System.out.printf("t=%4dms [%s] %s %s | %s%n", elapsedMs(), Thread.currentThread().getName(),
                snapshot.runId(), snapshot.status(), snapshot.detail());
    }

    private static long elapsedMs() {
        return (System.nanoTime() - START) / 1_000_000;
    }
}
