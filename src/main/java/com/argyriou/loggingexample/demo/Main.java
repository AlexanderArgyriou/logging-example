package com.argyriou.loggingexample.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

/**
 * CTRL + Click on debug method with placeholders vs a single string
 *
 * Log Level	Importance
 *
 * Fatal	One or more key business functionalities are not working and the whole system doesn’t fulfill the business functionalities.
 *
 * Error	One or more functionalities are not working, preventing some functionalities from working correctly.
 *
 * Warn	Unexpected behavior happened inside the application, but it is continuing its work and the key business features are operating as expected.
 *
 * Info	An event happened, the event is purely informative and can be ignored during normal operations.
 *
 * Debug	A log level used for events considered to be useful during software debugging when more granular information is needed.
 *
 * Trace	A log level describing events showing step by step execution of your code that can be ignored during the standard operation, but may be useful during extended debugging sessions.
 */
public class Main {
    public static final long ITERATIONS = 3000L;
    public static final int TESTS = 3;

    private static final Logger logger = LoggerFactory.getLogger("com.argyriou.log1");
    private static final Logger otherLogger = LoggerFactory.getLogger("com.argyriou.log2");

    public static void main(String[] args) {
        AtomicReference<List<Obj>> logChunk1 = new AtomicReference<>(new ArrayList<>());
        AtomicReference<List<Obj>> logChunk2 = new AtomicReference<>(new ArrayList<>());

        LongStream.range(0, ITERATIONS).parallel().forEach(i -> {
            logChunk1.set(getObjs());
            logChunk2.set(getObjs());
        });

        System.out.println("l1 chunk size : " + logChunk1.get().size());
        System.out.println("l2 chunk size : " + logChunk2.get().size());

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(() -> placeholders(logChunk1.get(), logChunk2.get()));
        executorService.submit(() -> concatenated(logChunk1.get(), logChunk2.get()));

        executorService.shutdown();
    }

    private static List<Obj> getObjs() {
        List<Obj> logChunk = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; ++i) {
            logChunk.add(new Obj());
        }
        return logChunk;
    }

    private static void placeholders(List<Obj> logChunk1, List<Obj> logChunk2) {
        long sum = 0L;
        for (int j = 0; j < TESTS; ++j) {
            long start = System.currentTimeMillis();
            LongStream.range(0, ITERATIONS).parallel().forEach(i -> {
                // logger.info("My objects are {} {}", logChunk1.get((int) i), logChunk2.get((int) i));
                // info is enabled these will not be executed and will not be evaluated also
                logger.debug("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "mary", "jane", "rock", "star");
                logger.trace("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "johny", "frost", "trap", "star");
                logger.debug("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "bat", "man", "rock", "star");
                logger.trace("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "ant", "man", "trap", "star");
                logger.debug("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "super", "man", "rock", "star");
                logger.trace("My objects are {} {} {} {} {} {}", logChunk1.get((int) i), logChunk2.get((int) i), "wonder", "woman", "trap", "star");
            });
            long end = System.currentTimeMillis();
            sum += end - start;
        }
        System.out.println("Placeholders time : " + sum / TESTS + "ms");
    }

    private static void concatenated(List<Obj> logChunk1, List<Obj> logChunk2) {
        long sum = 0L;
        for (int j = 0; j < TESTS; ++j) {
            long start = System.currentTimeMillis();
            LongStream.range(0, ITERATIONS).parallel().forEach(i -> {
                // otherLogger.info("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i));
                // info is enabled these will not be executed BUT IT WILL be evaluated :/
                otherLogger.debug("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "mary" + " " + "jane" + " " + "rock" + " " + "star");
                otherLogger.trace("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "johny" + " " + "frost" + " " + "trap" + " " + "star");
                otherLogger.debug("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "bat" + " " + "man" + " " + "rock" + " " + "star");
                otherLogger.trace("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "ant" + " " + "man" + " " + "trap" + " " + "star");
                otherLogger.debug("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "super" + " " + "man" + " " + "rock" + " " + "star");
                otherLogger.trace("My objects are " + logChunk1.get((int) i) + " " + logChunk2.get((int) i)
                        + " " + "wonder" + " " + "woman" + " " + "trap" + " " + "star");
            });
            long end = System.currentTimeMillis();
            sum += end - start;
        }
        System.out.println("Concatenated time : " + sum / TESTS + "ms");
    }
}
