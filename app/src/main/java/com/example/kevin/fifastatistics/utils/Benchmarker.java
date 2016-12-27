package com.example.kevin.fifastatistics.utils;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for benchmarking methods.
 */
public class Benchmarker {

    public static void time(final MethodRunner mr, final String tag) {
        long start = System.nanoTime();
        mr.call();
        long end = System.nanoTime();
        System.out.println(tag + ": " + TimeUnit.NANOSECONDS.toMillis(end - start) + "ms");
    }

    public interface MethodRunner {
        void call();
    }
}
